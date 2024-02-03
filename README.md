import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Main {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();

        // Volání služby A
        String taskIdA = client.target("url_sluzby_A")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(/* data pro službu A */), String.class);

        // Čekání na dokončení interního procesu ve službě B
        while (true) {
            // Volání služby A pro získání stavu
            String currentTaskId = client.target("url_sluzby_A")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(/* data pro získání stavu z A */), String.class);

            // Porovnání taskId - pokud je odlišný, proces byl dokončen
            if (!taskIdA.equals(currentTaskId)) {
                break;
            }

            // Počkejte nějaký čas před dalším opakováním (polling)
            try {
                Thread.sleep(1000); // počkejte 1 sekundu
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Pokračování s voláním služby C
        String responseC = client.target("url_sluzby_C")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(/* data pro službu C */), String.class);

        // Zpracování odpovědi ze služby C
        System.out.println(responseC);

        // Uzavření klienta
        client.close();
    }
}
