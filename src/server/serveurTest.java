
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class serveurTest {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");

        while (true) {
            try {

                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                // read request
                String line;
                line = in.readLine();
                StringBuilder raw = new StringBuilder();
                raw.append("" + line);
                boolean isPost;
                isPost = line.startsWith("POST");
                int contentLength = 0;
                while (!(line = in.readLine()).equals("")) {
                    raw.append('\n' + line);
                    if (isPost) {
                        final String contentHeader = "Content-Length: ";
                        if (line.startsWith(contentHeader)) {
                            contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                        }
                    }
                }
                StringBuilder body = new StringBuilder();
                if (isPost) {
                    int c = 0;
                    for (int i = 0; i < contentLength; i++) {
                        c = in.read();
                        body.append((char) c);
                    }
                    System.err.println("body : " + body);
                }
                raw.append(body.toString());
                //publishProgress(raw.toString());
                // send response
                out.write("HTTP/1.1 200 OK\r\n");
                out.write("Content-Type: text/html\r\n");
                out.write("\r\n");
                out.write(new Date().toString());
                if (isPost) {
                    out.write("<br><u>" + body.toString() + "</u>");
                } else {
                    out.write("<form method='POST'>");
                    out.write("<input name='fisrtname' type='text'/>");
                    out.write("<input name='lastname' type='text'/>");
                    out.write("<input name='email' type='text'/>");
                    out.write("<input type='submit'/>");
                    out.write("</form>");
                }
                // do not in.close();
                out.flush();
                out.close();
                socket.close();
                //
            } catch (Exception e) {
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                //publishProgress('\n' + sw.toString());
            }
        }

    }
}
