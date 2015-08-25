#Adobe Web Server

What is it?
------------
-HTTP/1.1 Based web server that serves, GET and POST requests. Also provides keep-alive functionality.

How to run?
------------
-Double clicking the executable file "adobe.webserver-0.0.1-SNAPSHOT-jar-with-dependencies.jar" will 
start the server in background.

External Dependencies and libraries used
-------------------------------------------
It uses log4J as it is a thread safe logging api. Junit for unit testing


How to configure?
-------------------
-A file named "server.properties" is located in the project's root folder which can be used to change 
the port number, the root website directory and a few more values.

What has been implemented?
---------------------------
- 
	1. Any file that will be kept within the root directory can be served via GET requests 
	(by specifying the filesystem URL e. if the file is located in rootDir/abc/xyz.html , it can be 
	accessed using the URL , http://localhost:portNumber/abc/xyz.html  (Firefox Recommended)
	2. Form Filling has been implemented. Any webpage that includes a form can be submitted 
	to the server and the data submitted through that form will be written to "Form Data.txt" located 
	within the root directory of the project.
	3. multipart/form-data is also supported. Any file (upto 250MB) can be uploaded to the server 
	which will be saved to the "UploadedFiles" folder. To test, open "post with file upload.html" 
	located within the root directory of the project.

(Refer to JavaDoc for more information. rootDir/doc/index.html)
