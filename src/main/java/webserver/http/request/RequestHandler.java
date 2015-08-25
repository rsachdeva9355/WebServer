package webserver.http.request;

/*
 * This class handles every request and decides which class needs to be invocated 
 * for the current request (GETHandler or POSTHandler)
 * 
 */

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import webserver.http.Server;
import webserver.http.parser.Parser;
import webserver.http.response.GETHandler;
import webserver.http.response.ResponseHeader;
import webserver.http.response.ResponseLine;
import Utilities.FileManager;



public class RequestHandler {
	String request = "";
	Parser parser;
	private static Logger log = Logger.getLogger(Server.class.getName());
	Map<String, String> requestHeaderMap, requestLineMap;
	DataInputStream in;
	Socket clientSocket;
	boolean isAValidRequest = false;
	int bufferSize = 65535;
	
	@SuppressWarnings("deprecation")
	/*
	 * Receives the socket and holds its input stream. Gets the request parameters and responds accordingly. 
	 */
	public RequestHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			clientSocket.setReceiveBufferSize(65536);
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			in = new DataInputStream(this.clientSocket.getInputStream());

			boolean isAFile = false;
			boolean isFormData = false;

			String input = "";
			input = in.readLine();
			request += input + "\n";

			StringTokenizer tokenizer = new StringTokenizer(input);
			String requestMethod = tokenizer.nextToken();
			// Check whether a GET Request!

			if (requestMethod.equals("GET")) {

				isAValidRequest = true;
				while ((input = in.readLine()) != null) {
					if (input.equals("")) {
						break;
					}
					request += input + "\n";
				}
			}

			// Check Whether a Post Request
			else if (requestMethod.equals("POST")) {
				isAValidRequest = true;
				String boundary = "", length = "";

				while ((input = in.readLine()) != null) {
					// System.out.println("Yes!" + x++);
					// Check whether a multipart file or a form data
					if (input.contains("Content-Type: multipart/form-data")) {
						boundary = input.split("; ")[1].split("=")[1];
						isAFile = true;
					} else if (input
							.contains("Content-Type: application/x-www-form-urlencoded")) {
						isFormData = true;
					}

					// Calculate content length
					if (input.contains("Content-Length:")) {
						length = input.split(": ")[1];
					}

					// Handling a File
					if (isAFile && input.contains("--" + boundary)) {
						multiPartFormData(boundary, length, in);
						break;
					}

					// Checking form
					else {
						if (input.equals("") && isFormData) {
							String file = "Form Data.txt";
							byte requestForm[] = new byte[Integer
									.parseInt(length)];
							in.read(requestForm, 0, Integer.parseInt(length));
							writeToFile(file, requestForm, "xyz");
							break;
						} else {
							request += input + "\r\n";
						}
					}
				}

			}
			else {
				System.out.println("Request Type : "+requestMethod);
				sendResponseToClient(501, "1.1", 1, false);
			}
			if (isAValidRequest) {
				parser = new Parser(request);
				requestHeaderMap = new HashMap<String, String>();
				requestLineMap = new HashMap<String, String>();
				try {
					requestHeaderMap = parser.getRequestHeaderMap();
					requestLineMap = parser.getRequestLineMap();
					isAValidRequest = true;

				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				} finally {

				}
			}

			if (requestMethod.equals("GET")) {
				respondToRequest();
			} else if (requestMethod.equals("POST")) {
				sendResponseToClient(200, "1.1", 0, isAFile);
			} 
		} catch (Exception e1) {
			e1.getLocalizedMessage();
		}

	}

	private void deleteFile(String fileName) {
		File file = new File(System.getProperty("user.dir") + File.separator
				+ fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	/*
	 * Very Important
	 * 
	 * Handles multipart/form-data uploads. Every file is downloaded in chunks of 64KB until complete.
	 */
	
	@SuppressWarnings("deprecation")
	private void multiPartFormData(String boundary, String length,
			DataInputStream in) {
		// here, we have the length of the boundary, we get the
		// firstLine,the second Line and everything else.
		try {
			String fLine = in.readLine();
			String fileName = fLine.split("filename=")[1].split("\"")[1];
			String sLine = in.readLine();
			in.readLine();

			long sizeToReduce = ("------" + boundary + boundary + fLine + sLine + "\r\n\r\n\r\n\r\n\r\n\r\n")
					.getBytes().length;
			long conLength = Long.parseLong(length);

			conLength -= sizeToReduce;

			long dataRead = 0;
			// If the file already exists, we might overwrite it.
			deleteFile(fileName);

			// read the file part by part!
			while (dataRead < conLength) {
				if (((conLength - dataRead) < bufferSize)
						&& (conLength - dataRead) > 0) {
					byte temp[] = new byte[(int) (conLength - dataRead)];
					if (in.available() == 0) {
						dataRead = conLength;
					}
					dataRead += (long) in.read(temp, 0,
							(int) (conLength - dataRead));
					writeToFile(fileName, temp, "xyz");
				} else {
					byte temp[] = new byte[bufferSize];
					dataRead += in.read(temp, 0, bufferSize);
					writeToFile(fileName, temp, "xyz");
					// System.out.println("Data read! : " + dataRead);
				}
			}
		} catch (Exception e) {

		}
	}

	public void writeToFile(String fileName, byte[] fileByteArray,
			String contentType) throws IOException {
		File file = new File(System.getProperty("user.dir") + File.separator + "UploadedFiles" + File.separator
				+ fileName);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file, true);
			output.write(fileByteArray);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			output.close();
		}

	}

	public String getRequestLine() {
		return requestLineMap.toString();
	}

	public String getRequestHeader() {
		return requestHeaderMap.toString();
	}

	public int respondToRequest() {

		if (isAValidRequest) {
			String requestMethod = requestLineMap.get(Parser.REQUEST_METHOD);
			switch (requestMethod) {
			case "GET": {
				GETHandler getHandler = new GETHandler(this.clientSocket,
						requestLineMap, requestHeaderMap, null);
				return getHandler.sendResponse();
			}
			case "POST": {
				System.out.println("Sending a response");
				break;
			}
			default: {
				return 0;
			}
			}
		}
		return 0;
	}

	
	//Default responses (File Upload complete, form submitted etc)
	
	public void sendResponseToClient(int status, String version, int error,
			boolean isAFile) {
		ResponseLine responseLine = new ResponseLine(status);
		String line = responseLine.getRequestLine(version);
		String fileRequested = "";

		if (error == 1) {
			fileRequested = Integer.toString(status) + ".html";
			System.out.println(fileRequested);
		}

		else if (isAFile && error == 0) {
			fileRequested = "fileuploadresponse.html";
		} 
		else {
			fileRequested = "formresponse.html";
		}
		File requestedFile = null;
		FileManager fileManager = new FileManager();
		if (error == 1) {
			requestedFile = fileManager.getErrorFile(fileRequested);
			System.out.println(requestedFile.getAbsolutePath());
		} else
			requestedFile = fileManager.getFile(fileRequested);

		ResponseHeader header = new ResponseHeader(requestedFile);
		System.out.println(header.getCompleteHeader() + "   "+requestedFile.getPath());
		// headerManager.setConnectionType(requestHeader.get("Connection").toString());
		String responseHeader = header.getCompleteHeader();
		try {
			OutputStream out = new BufferedOutputStream(
					clientSocket.getOutputStream());
			out.write(line.getBytes("ASCII"));
			out.write(responseHeader.getBytes("ASCII"));
			out.write(header.getFileByteArray());
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

