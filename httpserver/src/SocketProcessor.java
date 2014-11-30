import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import org.json.simple.*;
import java.net.*;


public class SocketProcessor implements Runnable {
    public Socket s;
    public InputStream is;
    public OutputStream os;
    public SocketProcessor(Socket s) throws Throwable {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }


    public void run() {
        try {
            String head = readInputHeaders();
            if(head.contains("POST")) System.err.println(head);
            if(head.contains("GET")){
                String URI = getURIFromHeader(head);
                writeResponse(URI);
            }
            if(head.contains("POST")){
                String URI = getURIFromHeader(head);
                if(head.contains("addEntry")){
                    head = head.substring(head.indexOf("json=")+5, head.length());
                    Object js = JSONValue.parse(head);
                    HttpServer.albums.add(new Album((JSONObject) js));
                    writeStringResponse("ok");
                    System.err.println("JSON object parsed. Total entries:" + HttpServer.albums.size());
                }
                else if (head.contains("getEntries")){
                    if (!HttpServer.albums.isEmpty()){

                        //String jsonArr = JSONArray.toJSONString(HttpServer.albums);
                        JSONArray arr = new JSONArray();
                        for(Album entry: HttpServer.albums){
                            arr.add(entry.toJSON());
                        }
                        writeStringResponse(arr.toJSONString());
                        System.err.println("JSON array sent on request.");
                    }
                    else{
                        writeStringResponse("no entries");
                    }
                }
                else if (head.contains("deleteEntry")){
                    head = head.substring(head.indexOf("json=")+5, head.length());
                    Object js = JSONValue.parse(head);
                    if(HttpServer.albums.remove(new Album((JSONObject) js)))
                        System.err.println("Entry removed. Total entries:" + HttpServer.albums.size());
                    writeStringResponse("deleted");
                }
                else {
                    head = head.substring(head.indexOf('?'), head.length());
                    Converter conv = new Converter(getResource(head, "number"),
                                    Integer.parseInt(getResource(head, "base1")),
                                    Integer.parseInt(getResource(head, "base2")));
                    writeStringResponse(conv.toFinalBase());
                }
            }

        } catch (Throwable t) {
                System.err.println(t.getMessage());
        } finally {
            try {
                s.close();
            } catch (Throwable t) {
                    /*do nothing*/
            }
        }
        System.err.println("Client processing finished");
    }

    public String getURIFromHeader(String header){
        int from = header.indexOf(" ")+2;
        return header.substring(from, header.indexOf(" ", from));
    }

    public String getResource(String data, String resource){
        String res;
        data = data.substring(data.indexOf(resource), data.length());
        int resEnd = data.indexOf("&")>0?data.indexOf("&"):data.length();
        res = data.substring(data.indexOf("=")+1, resEnd);
        return res;
    }

    public void writeLineResponse(String s) throws Throwable {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: 201\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "Content-Language: ru"+
                "Connection: close\r\n\r\n";
        String result = response + s;
        os.write(result.getBytes());
        os.flush();
    }

    public void writeResponse(String res) throws Throwable {
        byte[] buffer = new byte[1024];
//        String response = "HTTP/1.1 200 OK\r\n";
//        if(res.contains(".js"))
//            response += "Content-Type: text/javascript; charset=utf-8\r\n";
//        else if(res.contains(".css"))
//            response += "Content-Type: text/css; charset=utf-8\r\n";
//        else if(res.contains(".html"))
//            response += "Content-Type: text/html; charset=utf-8\r\n";
//        response +=
//                "Content-Language: ru\r\n"+
//                "Connection: close\r\n\r\n";
//        String result = response + s;
//        os.write(result.getBytes());

//        try{
//            fin = new FileInputStream("C:\\Users\\kostarew\\Desktop\\HTTP Server\\"+res);
//        }   catch (IOException e){
//            fin = new FileInputStream("C:\\Users\\kostarew\\Desktop\\HTTP Server\\main.html");
//        }
        InputStream fin = SocketProcessor.class.getClassLoader().getResourceAsStream("com/company/webFiles/"+res);
        if(fin==null)
            fin = SocketProcessor.class.getClassLoader().getResourceAsStream("com/company/webFiles/main.html");
        int bytesRead;

        while ((bytesRead = fin.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        fin.close();
        os.flush();
    }
    public void writeStringResponse(String res) throws Throwable {
        os.write(res.getBytes());
        os.flush();
    }
    public String readInputHeaders() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String builder="", s = null;
        int k = 0;
        while(true) {
            s = br.readLine();
            if(s == null || s.trim().length() == 0) {
                break;
            }
            builder += s + System.getProperty("line.separator");
        }
        if(builder.contains("getEntries"))
            return builder;
        if(builder.contains("POST")){
            char[] buffer = new char[16 << 10];
            int length = Integer.parseInt(getHeaderProperty(builder, "Content-Length"));
//            while (length > 0) {
//                String bodyLine = br.readLine();
//                length -= bodyLine.length() + 2;
//                if(bodyLine.length()>0){ builder += bodyLine; }
//            }
            int readLength;
            while(length > 0){
                readLength = Math.min(length, buffer.length);
                readLength = br.read(buffer, 0, readLength);
                if(readLength<0)
                    break;
                length -= readLength;
                String jsonVal = URLDecoder.decode(new String(buffer, 0, readLength),"UTF-8");
                builder += jsonVal;
            }
        }
        return builder;
}

//    public String readInputHeaders() throws Throwable {
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        StringBuilder builder =new StringBuilder();
//        String s = null;
//        while(true) {
//            s = br.readLine();
//            if(s == null || s.trim().length() == 0) {
//                break;
//            }
//            builder.append(s+System.getProperty("line.separator"));
//        }
//        return builder.toString();
//    }

    public String getHeaderProperty(String header, String property){
        while(!(header.startsWith(property)) || header.isEmpty()){
            header = header.substring(header.indexOf("\n")+1,header.length());
        }
        return header.substring(header.indexOf(' ')+1, header.indexOf("\r"));
    }

    public void processRequest(String head){

    }
    public String Convert(String num, String b1, String b2){
            Converter conv = new Converter(num, Integer.parseInt(b1), Integer.parseInt(b2));
            return conv.toFinalBase();
    }
}
