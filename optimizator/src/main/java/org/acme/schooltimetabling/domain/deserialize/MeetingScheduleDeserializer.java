package org.acme.schooltimetabling.domain.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.acme.schooltimetabling.domain.*;

import java.io.IOException;
import java.util.*;

public class MeetingScheduleDeserializer extends StdDeserializer<MeetingSchedule> {

    public MeetingScheduleDeserializer() {
        this(null);
    }

    public MeetingScheduleDeserializer(Class<?> vc) {
        super(vc);
    }


    @Override
    public MeetingSchedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        Map<UUID, OrganizacionaJedinica> orgJedinicaMap = new HashMap<>();
        ArrayNode departmanArrayNode = (ArrayNode) node.get("departmanList");
        for (Iterator<JsonNode> i = departmanArrayNode.elements(); i.hasNext();) {
            JsonNode departmanNode = i.next();
            UUID id = UUID.fromString(departmanNode.get("id").asText());
            String oznaka = departmanNode.get("oznaka").asText();
            String ssluzbaOznaka = departmanNode.get("ssluzbaOznaka").asText();
            String naziv = departmanNode.get("naziv").asText();
            Departman departman = new Departman(id, oznaka, ssluzbaOznaka, naziv);
            orgJedinicaMap.put(id, departman);
        }
        ArrayNode katedraArrayNode = (ArrayNode) node.get("katedraList");
        for (Iterator<JsonNode> i = katedraArrayNode.elements(); i.hasNext();) {
            JsonNode katedraNode = i.next();
            UUID id = UUID.fromString(katedraNode.get("id").asText());
            String oznaka = katedraNode.get("oznaka").asText();
            String ssluzbaOznaka = katedraNode.get("ssluzbaOznaka").asText();
            String naziv = katedraNode.get("naziv").asText();
            UUID departmanId = UUID.fromString(katedraNode.get("departman").asText());
            Departman departman = (Departman) orgJedinicaMap.get(departmanId);
            Katedra katedra = new Katedra(id, oznaka, ssluzbaOznaka, naziv, departman);
            departman.addKatedra(katedra);
            orgJedinicaMap.put(id, katedra);
        }

        Map<UUID, StudijskiProgram> studijskiProgramMap = new HashMap<>();
        ArrayNode studProgramArrayNode = (ArrayNode) node.get("studProgramList");
        for (Iterator<JsonNode> i = studProgramArrayNode.elements(); i.hasNext();) {
            // kreiraj studijsik program
            JsonNode studProgramNode = i.next();
            UUID id = UUID.fromString(studProgramNode.get("id").asText());
            int stepen = studProgramNode.get("stepen").intValue();
            int nivo = studProgramNode.get("nivo").intValue();
            String oznaka = studProgramNode.get("oznaka").asText();
            String naziv = studProgramNode.get("naziv").asText();
            StudijskiProgram studProgram = new StudijskiProgram(id, stepen, nivo, oznaka, naziv);
            studijskiProgramMap.put(id, studProgram);
        }

        Map<UUID, Predmet> predmetMap = new HashMap<>();
        ArrayNode predmetArrayNode = (ArrayNode) node.get("predmetList");
        for (Iterator<JsonNode> i = predmetArrayNode.elements(); i.hasNext();) {
            JsonNode predmetNode = i.next();
            UUID id = UUID.fromString(predmetNode.get("id").asText());
            String oznaka = predmetNode.get("oznaka").asText();
            int plan = predmetNode.get("plan").intValue();
            String naziv = predmetNode.get("naziv").asText();
            int godina = predmetNode.get("godina").intValue();
            String semestar = predmetNode.get("semestar").asText();
            String tipoviNastave = predmetNode.get("tipoviNastave").asText();
            int brojCasovaPred = predmetNode.get("brojCasovaPred").intValue();
            int brojCasovaAud = predmetNode.get("brojCasovaAud").intValue();
            int brojCasovaLab = predmetNode.get("brojCasovaLab").intValue();
            Predmet predmet = new Predmet(id, oznaka, plan, naziv, godina, semestar, tipoviNastave, brojCasovaPred, brojCasovaAud, brojCasovaLab);
            predmetMap.put(id, predmet);
        }

        Map<Integer, Dan> danMap = new HashMap<>();
        ArrayNode danArrayNode = (ArrayNode) node.get("danList");
        for (Iterator<JsonNode> i = danArrayNode.elements(); i.hasNext();) {
            JsonNode danNode = i.next();
            int danUNedelji = danNode.get("danUNedelji").intValue();
            Dan dan = new Dan(danUNedelji);
            danMap.put(danUNedelji, dan);
        }

        Map<UUID, TimeGrain> timeGrainMap = new HashMap<>();
        ArrayNode timeGrainArrayNode = (ArrayNode) node.get("timeGrainList");
        for (Iterator<JsonNode> i = timeGrainArrayNode.elements(); i.hasNext();) {
            JsonNode timeGrainNode = i.next();
            UUID id = UUID.fromString(timeGrainNode.get("id").asText());
            int grainIndex = timeGrainNode.get("grainIndex").intValue();
            int pocetniMinut = timeGrainNode.get("pocetniMinut").intValue();
            int danId = timeGrainNode.get("dan").intValue();
            Dan dan = danMap.get(danId);
            TimeGrain timeGrain = new TimeGrain(id, grainIndex, pocetniMinut, dan);
            timeGrainMap.put(timeGrain.getId(), timeGrain);
        }

        Map<UUID, Prostorija> prostorijaMap = new HashMap<>();
        ArrayNode prostorijaArrayNode = (ArrayNode) node.get("prostorijaList");
        for (Iterator<JsonNode> i = prostorijaArrayNode.elements(); i.hasNext();) {
            JsonNode prostorijaNode = i.next();
            UUID id = UUID.fromString(prostorijaNode.get("id").asText());
            String oznaka = prostorijaNode.get("oznaka").asText();
            TipProstorije tip = TipProstorije.valueOf(prostorijaNode.get("tip").asText());
            int kapacitet = prostorijaNode.get("kapacitet").intValue();
            // null ili lista
            List<OrganizacionaJedinica> orgJedinicaList = null;
            if (prostorijaNode.get("orgJedinica").isArray()) {
                orgJedinicaList = new ArrayList<>();
                ArrayNode orgJedinicaArrayNode = (ArrayNode) prostorijaNode.get("orgJedinica");
                for (Iterator<JsonNode> j = orgJedinicaArrayNode.elements(); j.hasNext();) {
                    JsonNode orgJedinicaNode = j.next();
                    UUID orgJedinicaId = UUID.fromString(orgJedinicaNode.asText());
                    orgJedinicaList.add(orgJedinicaMap.get(orgJedinicaId));
                }
            }
            Prostorija prostorija = new Prostorija(id, oznaka, tip, kapacitet, orgJedinicaList);
            prostorijaMap.put(prostorija.getId(), prostorija);
        }

        Map<UUID, Predavac> predavacMap = new HashMap<>();
        ArrayNode predavacArrayNode = (ArrayNode) node.get("predavacList");
        for (Iterator<JsonNode> i = predavacArrayNode.elements(); i.hasNext();) {
            JsonNode predavacNode = i.next();
            UUID id = UUID.fromString(predavacNode.get("id").asText());
            Long oznaka = (long) predavacNode.get("oznaka").asInt();
            String ime = predavacNode.get("ime").asText();
            String prezime = predavacNode.get("prezime").asText();
            String titula = predavacNode.get("titula").asText();
            boolean organizacijaFakulteta = predavacNode.get("organizacijaFakulteta").asBoolean();
            boolean dekanat = predavacNode.get("dekanat").asBoolean();
            UUID orgJedinicaId = UUID.fromString(predavacNode.get("orgJedinica").asText());
            OrganizacionaJedinica orgJedinica = orgJedinicaMap.get(orgJedinicaId);
            Predavac predavac = new Predavac(id, oznaka, ime, prezime, titula, orgJedinica, organizacijaFakulteta, dekanat);
            predavacMap.put(id, predavac);
        }

        Map<UUID, StudentskaGrupa> studentskaGrupaMap = new HashMap<>();
        ArrayNode studentskaGrupaArrayNode = (ArrayNode) node.get("studentskaGrupaList");
        for (Iterator<JsonNode> i = studentskaGrupaArrayNode.elements(); i.hasNext();) {
            JsonNode studentskaGrupaNode = i.next();
            UUID id = UUID.fromString(studentskaGrupaNode.get("id").asText());
            String oznaka = studentskaGrupaNode.get("oznaka").asText();
            int godina = studentskaGrupaNode.get("godina").asInt();
            String semestar = studentskaGrupaNode.get("semestar").asText();
            int brojStudenata = studentskaGrupaNode.get("brojStudenata").asInt();
            UUID studProgramId = UUID.fromString(studentskaGrupaNode.get("studijskiProgram").asText());
            StudijskiProgram studijskiProgram = studijskiProgramMap.get(studProgramId);
            StudentskaGrupa studentskaGrupa = new StudentskaGrupa(id, oznaka, godina, semestar, brojStudenata, studijskiProgram);
            studentskaGrupaMap.put(id, studentskaGrupa);
        }

        Map<UUID, Meeting> meetingMap = new HashMap<>();
        ArrayNode meetingArrayNode = (ArrayNode) node.get("meetingList");
        for (Iterator<JsonNode> i = meetingArrayNode.elements(); i.hasNext();) {
            JsonNode meetingNode = i.next();
            UUID id = UUID.fromString(meetingNode.get("id").asText());
            TipProstorije tipProstorije = TipProstorije.valueOf(meetingNode.get("tipProstorije").asText());
            MeetingType meetingTip = MeetingType.valueOf(meetingNode.get("meetingTip").asText());
            int brojCasova = meetingNode.get("brojCasova").asInt();
            UUID predavacId = UUID.fromString(meetingNode.get("predavac").asText());
            Predavac predavac = predavacMap.get(predavacId);

            // ostaliPredavaci -> [""] ili lista ["guid", "guid"]
            List<Predavac> ostaliPredavaci = new ArrayList<>();
            ArrayNode ostaliPredavaciArrayNode = (ArrayNode) meetingNode.get("ostaliPredavaci");
            for (Iterator<JsonNode> j = ostaliPredavaciArrayNode.elements(); j.hasNext();) {
                JsonNode ostaliPredavacNode = j.next();
                if (!ostaliPredavacNode.asText().equals("")) {
                    UUID ostaliPredavacId = UUID.fromString(ostaliPredavacNode.asText());
                    ostaliPredavaci.add(predavacMap.get(ostaliPredavacId));
                }
            }
            UUID predmetId = UUID.fromString(meetingNode.get("predmet").asText());
            Predmet predmet = predmetMap.get(predmetId);
            // studGrupe -> guid ili lista
            List<StudentskaGrupa> studGrupeList = new ArrayList<>();
            if (meetingNode.get("studentskeGrupe").isArray()) {
                ArrayNode studGrupeArrayNode = (ArrayNode) meetingNode.get("studentskeGrupe");
                for (Iterator<JsonNode> j = studGrupeArrayNode.elements(); j.hasNext();) {
                    JsonNode studGrupaNode = j.next();
                    UUID studGrupaId = UUID.fromString(studGrupaNode.asText());
                    studGrupeList.add(studentskaGrupaMap.get(studGrupaId));
                }
            } else {
                UUID studGrupaId = UUID.fromString(meetingNode.get("studentskeGrupe").asText());
                studGrupeList.add(studentskaGrupaMap.get(studGrupaId));
            }
            boolean biWeekly = meetingNode.get("biWeekly").asBoolean();
            Meeting meeting = new Meeting(id, tipProstorije, meetingTip, brojCasova, predavac, ostaliPredavaci,
                    predmet, studGrupeList, biWeekly);
            meetingMap.put(meeting.getId(), meeting);
        }

        // meeting assignment list -> when some entities are already planned, just add them to the list
        List<MeetingAssignment> meetingAssignmentList = new ArrayList<>();
        ArrayNode meetingAssignmentArrayNode = (ArrayNode) node.get("meetingAssignmentList");
        if (meetingAssignmentArrayNode != null) {
            for (Iterator<JsonNode> i = meetingAssignmentArrayNode.elements(); i.hasNext(); ) {
                JsonNode meetingAssignmentNode = i.next();
                // MeetingAssignment id
                UUID id = UUID.fromString(meetingAssignmentNode.get("id").asText());
                // Meeting id
                UUID meetingId = UUID.fromString(meetingAssignmentNode.get("meeting").get("id").asText());
                Meeting meeting = meetingMap.get(meetingId);
                // remove from map so only unassigned entities are left in map
                meetingMap.remove(meetingId);
                // StartingTimeGrain id
                UUID startingTimeGrainId = UUID.fromString(meetingAssignmentNode.get("startingTimeGrain").get("id").asText());
                TimeGrain startingTimeGrain = timeGrainMap.get(startingTimeGrainId);
                // Prostorija id
                UUID prostorijaId = UUID.fromString(meetingAssignmentNode.get("prostorija").get("id").asText());
                Prostorija prostorija = prostorijaMap.get(prostorijaId);
                // MeetingAssignment
                MeetingAssignment meetingAssignment = new MeetingAssignment(id, meeting, startingTimeGrain, prostorija);
                meetingAssignmentList.add(meetingAssignment);
            }
        }
        // add unassigned entities to all entities
        for (Meeting meeting : meetingMap.values()) {
            MeetingAssignment meetingAssignment = new MeetingAssignment(meeting);
            meetingAssignmentList.add(meetingAssignment);
        }

        Semestar semestar = Semestar.valueOf(node.get("semestar").asText());

        return new MeetingSchedule(
                new ArrayList<>(studijskiProgramMap.values()),
                new ArrayList<>(meetingMap.values()),
                new ArrayList<>(danMap.values()),
                new ArrayList<>(timeGrainMap.values()),
                new ArrayList<>(prostorijaMap.values()),
                new ArrayList<>(predavacMap.values()),
                new ArrayList<>(studentskaGrupaMap.values()),
                meetingAssignmentList,
                semestar
                );
    }

}
