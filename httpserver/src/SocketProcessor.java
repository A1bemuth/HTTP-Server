import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.*;
import freemarker.template.*;


public class SocketProcessor implements Runnable {
    public Socket s;
    public InputStream is;
    public OutputStream os;
    private Template htmlTemplate = HttpServer.cfg.getTemplate("main.ftl");

    public SocketProcessor(Socket s) throws Throwable {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }

    public void run() {
        try {
            String head = readRequest();
            processRequest(head);
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

    /**
     * Выбирает метод обработки запроса по заголовку и запрошенному ресурсу.
     */
    private void processRequest(String head) throws Throwable {
        if(head.contains("GET")){
            writeResponse(getURIFromHeader(head));
        }
        if(head.contains("POST")){

            if(head.contains("addEntry")){
                addEntry(head);
            }
            else if (head.contains("getEntries")){
                getEntries();
            }
            else if (head.contains("deleteEntry")){
                deleteEntry(head);
            }
            else {
                convertNumber(head);
            }
        }
    }

    /**
     * Конвертирует число в новую систему счисления.
     */
    private void convertNumber(String head) throws Throwable {
        head = head.substring(head.indexOf('?'), head.length());
        Converter conv = new Converter(getResourceFromHead(head, "number"),
                        Integer.parseInt(getResourceFromHead(head, "base1")),
                        Integer.parseInt(getResourceFromHead(head, "base2")));
        writeStringResponse(conv.toFinalBase());
    }

    /**
     * Удаляет запись из списка альбомов и оповещает клиента об успехе.
     */
    private void deleteEntry(String head) throws Throwable {
        head = head.substring(head.indexOf("json=")+5, head.length());
        Object js = JSONValue.parse(head);
        if(HttpServer.albums.remove(new Album((JSONObject) js)))
            System.err.println("Entry removed. Total entries:" + HttpServer.albums.size());
        writeStringResponse("deleted");
    }

    /**
     * Отправляет клиенту содержимое списка альбомов в формате json.
     */
    private void getEntries() throws Throwable {
        if (!HttpServer.albums.isEmpty()){
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

    /**
     * Добавляет запись в список альбомов.
     */
    private void addEntry(String head) throws Throwable {
        head = head.substring(head.indexOf("json=")+5, head.length());
        Object js = JSONValue.parse(head);
        HttpServer.albums.add(new Album((JSONObject) js));
        writeStringResponse("ok");
        System.err.println("JSON object parsed. Total entries:" + HttpServer.albums.size());
    }

    public String getURIFromHeader(String header){
        int from = header.indexOf(" ")+2;
        return header.substring(from, header.indexOf(" ", from));
    }

    /**
     * Извлекает указанный ресурс из строки запроса в формате "&resource1=value1&resource2=value2&..".
     */
    public String getResourceFromHead(String data, String resource){
        String res;
        data = data.substring(data.indexOf(resource), data.length());
        int resEnd = data.indexOf("&")>0?data.indexOf("&"):data.length();
        res = data.substring(data.indexOf("=")+1, resEnd);
        return res;
    }

    /**
     * Отправляет клиенту файл, указанный в URI.
     */
    public void writeResponse(String uri) throws Throwable {
        byte[] buffer = new byte[1024];

        InputStream fileInput = SocketProcessor.class.getClassLoader().getResourceAsStream("com/company/webFiles/"+uri);

        if(uri.contains("main.html")){
            writeResponseHeader(uri,"UTF-8","200 OK");
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(os));
            htmlTemplate.process(getData(), bf);
            bf.flush();
            bf.close();
            return;
        }

        if(fileInput==null) {
            fileInput = SocketProcessor.class.getClassLoader().getResourceAsStream("com/company/webFiles/main.html");
            writeResponseHeader("main.html","","404 Not Found");
        }
        else{
            writeResponseHeader(uri, "UTF-8", "200 OK");
        }

        int bytesRead;
        while ((bytesRead = fileInput.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        fileInput.close();
        os.flush();
    }

    private Map getData(){
        Map root = new HashMap();
        Map authors = new HashMap();

        root.put("authors", authors);
        authors.put("first", "Костарев Д.С.");
        authors.put("second", "Дудин Н.А.");
        return root;
    }

    /**
     * Отправляет заголовок ответа на запрос, соответствующий типу содержимого.
     */
    public void writeResponseHeader(String uri, String encoding, String status) throws Throwable {
        String contentType = "text/plain";

        if (uri.contains("html"))
            contentType = "text/html" + (encoding.isEmpty() ? "" : (";charset="+encoding));
        else if (uri.contains("css"))
            contentType = "text/css";
        else if (uri.contains("js"))
            contentType = "application/javascript";
        else if (uri.contains("jpg"))
            contentType = "image/jpeg";
//        else writeStringResponse(uri + " 200 OK");
        String response = "HTTP/1.1 " + status + "\r\n" +
            "Content-Type: "+contentType+"\r\n" +
            "Connection: Keep-Alive \r\n" +
            "Content-Language: ru\r\n\r\n";
        os.write(response.getBytes());
        os.flush();
    }

    /**
     * Отправляет клиенту строку.
     */
    public void writeStringResponse(String message) throws Throwable {
        os.write(message.getBytes());
        os.flush();
    }

    /**
     * Считывает запрос клиента.
     */
    public String readRequest() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String builder="", s = null;

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
            builder = readPostBody(br, builder);
        }
        return builder;
    }

    private String readPostBody(BufferedReader br, String builder) throws IOException {
        char[] buffer = new char[16 << 10];
        int length = Integer.parseInt(getHeaderProperty(builder, "Content-Length"));
        int readLength;
        while(length > 0){
            readLength = Math.min(length, buffer.length);
            readLength = br.read(buffer, 0, readLength);
            if(readLength < 0)
                break;
            length -= readLength;
            String jsonVal = URLDecoder.decode(new String(buffer, 0, readLength), "UTF-8");
            builder += jsonVal;
        }
        return builder;
    }

    /**
     * Извлекает указанное свойство из заголовка запроса.
     */
    public String getHeaderProperty(String header, String property){
        while(!(header.startsWith(property)) || header.isEmpty()){
            header = header.substring(header.indexOf("\n")+1,header.length());
        }
        return header.substring(header.indexOf(' ') + 1, header.indexOf("\r"));
    }
}
