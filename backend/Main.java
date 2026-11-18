import com.sun.net.httpserver.HttpServer;
public class Main {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);
        server.createContext("/leads", exchange -> {

    try {

        String response = LeadDAO.viewLeads();
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.length());

        exchange.getResponseBody().write(response.getBytes());

        exchange.close();

    } catch (Exception e) {

        e.printStackTrace();

    }
});

     server.createContext("/addLead", exchange -> {

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Origin", "*"
    );

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Methods", "POST, OPTIONS"
    );

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Headers", "Content-Type"
    );

    if ("OPTIONS".equals(exchange.getRequestMethod())) {
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
        return;
    }

    if ("POST".equals(exchange.getRequestMethod())) {

        String requestBody = new String(
            exchange.getRequestBody().readAllBytes(),
            java.nio.charset.StandardCharsets.UTF_8
        );

        String[] fields = requestBody.split("&");

        Lead lead = new Lead();

        for (String field : fields) {

            String[] parts = field.split("=", 2);

            String key = java.net.URLDecoder.decode(
                parts[0], "UTF-8"
            );

            String value = parts.length > 1
                ? java.net.URLDecoder.decode(parts[1], "UTF-8")
                : "";

            switch (key) {
                case "name": lead.name = value; break;
                case "email": lead.email = value; break;
                case "phone": lead.phone = value; break;
                case "company": lead.company = value; break;
            }
        }

        try {
                LeadDAO.addLead(lead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = "Lead added successfully!";

        exchange.sendResponseHeaders(200, response.length());

        exchange.getResponseBody().write(response.getBytes());

        exchange.close();
    }
});
server.createContext("/updateLead", exchange -> {

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Origin", "*"
    );

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Methods", "POST, OPTIONS"
    );

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Headers", "Content-Type"
    );

    if ("OPTIONS".equals(exchange.getRequestMethod())) {
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
        return;
    }

    if ("POST".equals(exchange.getRequestMethod())) {

        String requestBody = new String(
            exchange.getRequestBody().readAllBytes(),
            java.nio.charset.StandardCharsets.UTF_8
        );

        int id = 0;
        String name = "";
        String email = "";
        String phone = "";
        String company = "";

        String[] fields = requestBody.split("&");

        for (String field : fields) {

            String[] parts = field.split("=", 2);

            String key = java.net.URLDecoder.decode(
                parts[0], "UTF-8"
            );

            String value = parts.length > 1
                ? java.net.URLDecoder.decode(parts[1], "UTF-8")
                : "";

            switch (key) {
                case "id": id = Integer.parseInt(value); break;
                case "name": name = value; break;
                case "email": email = value; break;
                case "phone": phone = value; break;
                case "company": company = value; break;
            }
        }

        try {

            LeadDAO.updateLead(
                id, name, email, phone, company
            );

            String response = "Lead updated successfully!";

            exchange.sendResponseHeaders(200, response.length());

            exchange.getResponseBody().write(response.getBytes());

        } catch (Exception e) {

            e.printStackTrace();

            String response = "Error updating lead";

            exchange.sendResponseHeaders(500, response.length());

            exchange.getResponseBody().write(response.getBytes());
        }

        exchange.close();
    }
});
        server.start();

     server.createContext("/deleteLead", exchange -> {

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Origin", "*"
    );

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Methods", "DELETE, OPTIONS"
    );

    exchange.getResponseHeaders().add(
        "Access-Control-Allow-Headers", "Content-Type"
    );

    if ("OPTIONS".equals(exchange.getRequestMethod())) {
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
        return;
    }

    if ("DELETE".equals(exchange.getRequestMethod())) {

        String query = exchange.getRequestURI().getQuery();

        int id = Integer.parseInt(
            query.substring(query.indexOf("=") + 1)
        );

        try {
            LeadDAO.deleteLead(id);

            String response = "Lead deleted successfully!";

            exchange.sendResponseHeaders(
                200,
                response.getBytes().length
            );

            exchange.getResponseBody().write(
                response.getBytes()
            );

        } catch (Exception e) {
            e.printStackTrace();

            String response = "Error deleting lead";

            exchange.sendResponseHeaders(
                500,
                response.getBytes().length
            );

            exchange.getResponseBody().write(
                response.getBytes()
            );
        }

        exchange.close();
    }
});

        System.out.println("Server started on port 8080");

    }
}