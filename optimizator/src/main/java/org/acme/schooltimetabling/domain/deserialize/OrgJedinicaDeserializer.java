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
import java.util.UUID;

public class OrgJedinicaDeserializer extends StdDeserializer<OrganizacionaJedinica> {

    public OrgJedinicaDeserializer() {
        this(null);
    }

    public OrgJedinicaDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public OrganizacionaJedinica deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        UUID id = UUID.fromString(node.get("id").asText());
        String oznaka = node.get("oznaka").asText();
        String ssluzbaOznaka = node.get("ssluzbaOznaka").asText();
        String naziv = node.get("naziv").asText();
        if (node.get("departman").isNull()) {
            return new Departman(id, oznaka, ssluzbaOznaka, naziv);
        }
        JsonNode departmanNode = node.get("departman");
        UUID departmanId = UUID.fromString(departmanNode.get("id").asText());
        String departmanOznaka = departmanNode.get("oznaka").asText();
        String departmanSsluzbaOznaka = departmanNode.get("ssluzbaOznaka").asText();
        String departmanNaziv = departmanNode.get("naziv").asText();
        Departman departman = new Departman(departmanId, departmanOznaka, departmanSsluzbaOznaka, departmanNaziv);
        return new Katedra(id, oznaka, ssluzbaOznaka, naziv, departman);
    }
}
