package org.ekipaenajst.beans;

import org.ekipaenajst.entitete.Oddaljenost;
import org.ekipaenajst.entitete.Parkirisce;
import org.ekipaenajst.entitete.Zasedenost;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SortLogic implements Serializable {
    @PersistenceContext(unitName = "advisor-jpa")
    private EntityManager em;

    private String mapURL;

    private HttpClient httpClient;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {

        objectMapper = new ObjectMapper();

        httpClient = HttpClient.newBuilder().build();

        Map<String,String> env = System.getenv();

        mapURL = env.get("MAP_URL");
    }

    public int superAwesomesauceComparator(Parkirisce p1, Parkirisce p2, String param) {
        // neodvisno od vsega potisne zasedena parkiriča na dno
        Zasedenost zas1 = p1.getZasedenost();
        Zasedenost zas2 = p2.getZasedenost();
        int z1 = (zas1.getDnevniUporabniki().getProsta() == 0 || zas1.getAbonenti().getProsta() == 0) ? 0 : 1;
        int z2 = (zas2.getDnevniUporabniki().getProsta() == 0 || zas2.getAbonenti().getProsta() == 0) ? 0 : 1;
        if (z1*z2 == 0) return z1-z2;

        switch (param) {
            case "oddaljenost": {
                Oddaljenost odd1 = p1.getOddaljenost();
                Oddaljenost odd2 = p2.getOddaljenost();
                long c = odd1.getRazdaljaMetri() - odd2.getRazdaljaMetri();
                return c > 0 ? 1 : (c < 0 ? -1 : 0);
            }
            case "zasedenost": { // ?
                return zas1.getDnevniUporabniki().getProsta() - zas2.getDnevniUporabniki().getProsta();
            }
            case "cena_default": {
                int t = LocalTime.now().getHour();
                double pr1 = (p1.getZacetekDneva() <= t && p1.getKonecDneva() > t) ? p1.getCenaDefault() : p1.getCenaNocna();
                double pr2 = (p2.getZacetekDneva() <= t && p2.getKonecDneva() > t) ? p2.getCenaDefault() : p2.getCenaNocna();
                double c = pr1 - pr2;
                return c > 0 ? 1 : (c < 0 ? -1 : 0);
            }
            case "cena_short": {
                int t = LocalTime.now().getHour();
                double pr1 = p1.getCenaDnevnaPrviDveUri()*2+p1.getCenaDnevnaTretjaUra();
                double pr2 = p2.getCenaDnevnaPrviDveUri()*2+p2.getCenaDnevnaTretjaUra();
                pr1 = (p1.getZacetekDneva() <= t && p1.getKonecDneva() > t) ? pr1 : p1.getCenaNocna();
                pr2 = (p2.getZacetekDneva() <= t && p2.getKonecDneva() > t) ? pr2 : p2.getCenaNocna();
                double c = pr1 - pr2;
                return c > 0 ? 1 : (c < 0 ? -1 : 0);
            }
            default:
                return 0;
        }
    }

    @Transactional
    public Parkirisce[] getSortedParkirisca(String[] sortparams) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(mapURL))
                    .headers("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Parkirisce[] parkirisca = objectMapper.readValue(response.body(), Parkirisce[].class);
            Arrays.sort(parkirisca, new Comparator<Parkirisce>() {
                @Override
                public int compare(Parkirisce p1, Parkirisce p2) {
                    for (String param: sortparams) {
                        int comp = superAwesomesauceComparator(p1, p2, param);
                        if (comp != 0) return comp;
                    }
                    return 0;
                }
            });
            return parkirisca;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
