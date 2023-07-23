package org.acme.schooltimetabling.domain.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.acme.schooltimetabling.domain.Departman;
import org.acme.schooltimetabling.domain.Katedra;
import org.acme.schooltimetabling.domain.OrganizacionaJedinica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrgJedinicaDeserializer extends StdDeserializer<OrganizacionaJedinica> {

    private final Map<UUID, Departman> departmanMap;

    public OrgJedinicaDeserializer() {
        this(null);
    }

    public OrgJedinicaDeserializer(Class<?> vc) {
        super(vc);
        this.departmanMap = new HashMap<>();
    }

    @Override
    public OrganizacionaJedinica deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        UUID id = UUID.fromString(node.get("id").asText());
        String oznaka = node.get("oznaka").asText();
        String ssluzbaOznaka = node.get("ssluzbaOznaka").asText();
        String naziv = node.get("naziv").asText();
        JsonNode departmanNode = node.get("departman");
        if (departmanNode == null || departmanNode.isNull()) {
            Departman departman = new Departman(id, oznaka, ssluzbaOznaka, naziv);
            departmanMap.put(departman.getId(), departman);
            return departman;
        }
        UUID departmanId = UUID.fromString(departmanNode.get("id").asText());
        Departman departman = departmanMap.get(departmanId);
        if (departman == null) {
            String departmanOznaka = departmanNode.get("oznaka").asText();
            String departmanSsluzbaOznaka = departmanNode.get("ssluzbaOznaka").asText();
            String departmanNaziv = departmanNode.get("naziv").asText();
            departman = new Departman(departmanId, departmanOznaka, departmanSsluzbaOznaka, departmanNaziv);
            departmanMap.put(departman.getId(), departman);
        }
        Katedra katedra = new Katedra(id, oznaka, ssluzbaOznaka, naziv, departman);
        departman.addKatedra(katedra);
        return katedra;
    }
}
