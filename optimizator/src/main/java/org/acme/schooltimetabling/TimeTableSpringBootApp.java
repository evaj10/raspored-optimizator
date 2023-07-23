package org.acme.schooltimetabling;

import org.acme.schooltimetabling.domain.*;
import org.acme.schooltimetabling.solver.TimeTableState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TimeTableSpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(TimeTableSpringBootApp.class, args);
    }

    @Bean
    public TimeTableState state() {
        return new TimeTableState();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Bean
    public MeetingSchedule demoData() {
        Departman rac = new Departman("021", "21", "Departman za racunarstvo i informatiku");
        Katedra informatika = new Katedra("216", "110", "Katedra za informatiku", rac);
        rac.addKatedra(informatika);

        List<StudijskiProgram> programi = new ArrayList<>();
        StudijskiProgram siit = new StudijskiProgram(1, 1, "SE1", "Softversko izenjerstvo i informacione tehnologije");
        programi.add(siit);

        List<Prostorija> prostorije = new ArrayList<>();
        // racunarske - katedra za informatiku
        Prostorija p1 = new Prostorija("NTP-307", TipProstorije.RAC, 16, List.of(informatika));
        Prostorija p2 = new Prostorija("NTP-309", TipProstorije.RAC, 16, List.of(informatika));
        Prostorija p3 = new Prostorija("NTP-311", TipProstorije.RAC, 16, List.of(informatika));
        Prostorija p4 = new Prostorija("INT-1", TipProstorije.RAC, 16, List.of(informatika));
        // auditorne
        Prostorija p5 = new Prostorija("101", TipProstorije.AUD, 100, null);
        Prostorija p6 = new Prostorija("204", TipProstorije.AUD, 126, null);
        Prostorija p7 = new Prostorija("205", TipProstorije.AUD, 122, null);
        Prostorija p8 = new Prostorija("AH1B", TipProstorije.AUD, 110, null);
        Prostorija p9 = new Prostorija("NTP-001", TipProstorije.AUD, 80, null);
        Prostorija p10 = new Prostorija("NTP-417", TipProstorije.AUD, 64, null);

        prostorije.add(p1);
        prostorije.add(p2);
        prostorije.add(p3);
        prostorije.add(p4);
        prostorije.add(p5);
        prostorije.add(p6);
        prostorije.add(p7);
        prostorije.add(p8);
        prostorije.add(p9);
        prostorije.add(p10);

        // 38 sati predavanja na OAS
        // 165 sati vezbi na OAS
        // 8 sati predavanja na MAS
        // 10.25 sati vezbi na MAS

        List<Predmet> predmeti = new ArrayList<>();
        // 1. godina
        Predmet mreze = new Predmet("SIT021", 1, "Internet mreze", 1, "L", "P|R", 2, 0, 2); //1:45 = 105 = 7 (+1)
        Predmet wd = new Predmet("WD", 1, "Web dizajn", 1, "L", "P|R", 2, 0, 2); //1:45 = 105 = 7 (+1)
        Predmet oop = new Predmet("OOP1", 1, "Objektno orijentisano programiranje 1", 1, "L", "P|R", 3, 0, 2); //2:45 = 165 = 11 (+1)
        Predmet asp = new Predmet("ASP", 1, "Algoritmi i strukture podataka", 1, "L", "P|R", 3, 0, 2); //2:45 = 165 = 11 (+1)

        // 2. godina
        Predmet sims = new Predmet("SIMS", 1, "Specifikacija i modeliranje softvera", 2, "L", "P|R", 3, 0, 3); //2:45 = 165 = 11 (+1)
        Predmet usi = new Predmet("USI", 1, "Uvod u softversko inzenjerstvo", 2, "L", "P|R", 3, 0, 2); //2:45 = 165 = 11 (+1)
        Predmet ui = new Predmet("UI", 1, "Upravljanje informacijama", 2, "L", "P|R", 2, 0, 2); //1:45 = 105 = 7 (+1)

        // 3. godina
        Predmet mrs = new Predmet("MRS", 1, "Metodologije razvoja softvera", 3, "L", "P|R", 2, 0, 3); //1:45 = 105 = 7 (+1)
        Predmet ori = new Predmet("ORI", 1, "Osnovi racunarske inteligencije", 3, "L", "P|R", 2, 0, 4); //1:45 = 105 = 7 (+1)    * 2
        Predmet pigkut = new Predmet("PIGKUT", 1, "Pisana i govorna komunikacija u tehnici", 3, "L", "P|R", 2, 0, 2); //1:45 = 105 = 7 (+1)
        Predmet isa = new Predmet("ISA", 1, "Internet softverske arhitekture", 3, "L", "P|R", 2, 0, 3); //1:45 = 105 = 7 (+1)

        // 4. godina
        Predmet bsep = new Predmet("BSEP", 1, "Bezbednost u sistemima elektronskog poslovanja", 4, "L", "P|R", 3, 0, 3); //2:45 = 165 = 11 (+1)
        Predmet ntp = new Predmet("NTP", 1, "Napredne tehnike programiranja", 4, "L", "P|R", 2, 0, 2); //1:45 = 105 = 7 (+1)
        Predmet ml = new Predmet("ML", 1, "Masinsko ucenje", 4, "L", "P|R", 3, 0, 2); //2:45 = 165 = 11 (+1)
        Predmet sbnz = new Predmet("SBNZ", 1, "Sistemi bazirani na znanju", 4, "L", "P|R", 3, 0, 3); //2:45 = 165 = 11 (+1)

        // master
        Predmet ntri = new Predmet("NTRI", 1, "Napredne tehnike racunarske inteligencije", 5, "L", "P|R", 3, 0, 3); //2:45 = 165 = 11 (+1)
        Predmet devops = new Predmet("DEVOPS", 1, "Integrisani pristup razvoju softvera DevOps", 5, "L", "P|R", 2, 0, 2); //1:45 = 105 = 7 (+1)
        Predmet udf = new Predmet("UDF", 1, "Uvod u digitalnu forenziku", 5, "L", "P|R", 3, 0, 3); //2:45 = 165 = 11 (+1)

        predmeti.add(mreze);
        predmeti.add(wd);
        predmeti.add(oop);
        predmeti.add(asp);
        predmeti.add(sims);
        predmeti.add(usi);
        predmeti.add(ui);
        predmeti.add(mrs);
        predmeti.add(ori);
        predmeti.add(pigkut);
        predmeti.add(isa);
        predmeti.add(bsep);
        predmeti.add(ntp);
        predmeti.add(ml);
        predmeti.add(sbnz);
        predmeti.add(ntri);
        predmeti.add(devops);
        predmeti.add(udf);


        // profesori
        Predavac zaric = new Predavac(1L, "Miroslav", "Zaric", "dr", false, false, informatika);
        Predavac minja = new Predavac(2L, "Milan", "Vidakovic", "dr", true, false, informatika);
        Predavac zeljko = new Predavac(3L, "Zeljko", "Vukovic", "dr", false, false, informatika);
        Predavac branko = new Predavac(3L, "Branko", "Milosavljevic", "dr", false, false, informatika);
        Predavac goca = new Predavac(5L, "Gordana", "Milosavljevic", "dr", false, false, informatika);
        Predavac luburic = new Predavac(6L, "Nikola", "Luburic", "dr", false, false, informatika);
        Predavac gostojic = new Predavac(7L, "Stevan", "Gostojic", "dr", false, false, informatika);
        Predavac sladic = new Predavac(8L, "Goran", "Sladic", "dr", true, false, informatika);
        Predavac koca = new Predavac(9L, "Aleksandar", "Kovacevic", "dr", false, false, informatika);
        Predavac slivka = new Predavac(10L, "Jelena", "Slivka", "dr", false, false, informatika);
        Predavac dejanovic = new Predavac(11L, "Igor", "Dejanovic", "dr", false, false, informatika);
        Predavac sinisa = new Predavac(12L, "Sinisa", "Nikolic", "dr", false, false, informatika);
        // asistenti
        Predavac eva = new Predavac(13L, "Eva", "Jankovic", false, false, informatika);
        Predavac mitar = new Predavac(14L, "Mitar", "Perovic", false, false, informatika);
        Predavac tamara = new Predavac(15L, "Tamara", "Kovacevic", false, false, informatika);
        Predavac alek = new Predavac(16L, "Aleksandar", "Vujinovic", false, false, informatika);
        Predavac ivan = new Predavac(17L, "Ivan", "Peric", false, false, informatika);
        Predavac mina = new Predavac(18L, "Mina", "Vejnovic", false, false, informatika);
        Predavac marko = new Predavac(19L, "Marko", "Raseta",false, false, informatika);
        Predavac balsa = new Predavac(20L, "Balsa", "Sarenac",false, false, informatika);
        Predavac veljko = new Predavac(21L, "Veljko", "Maksimovic", false, false, informatika);
        Predavac dragan = new Predavac(22L, "Dragan", "Vidakovic", false, false, informatika);
        Predavac bane = new Predavac(23L, "Branislav", "Andjelic", false, false, informatika);
        Predavac dusan = new Predavac(24L, "Dusan", "Nikolic", false, false, informatika);
        Predavac aleksandar = new Predavac(25L, "Aleksandar", "Lukic", false, false, informatika);
        Predavac budjen = new Predavac(26L, "Vladimir", "Budjen", false, false, informatika);
        Predavac katarina = new Predavac(27L, "Katarina-Glorija", "Grujic", false, false, informatika);
        Predavac andjela = new Predavac(28L, "Andjela", "Trajkovic", false, false, informatika);
        Predavac milica = new Predavac(29L, "Milica", "Skipina", false, false, informatika);
        Predavac marija = new Predavac(30L, "Marija", "Kovacevic", false, false, informatika);
        Predavac indjic = new Predavac(31L, "Vladimir", "Indjic", false, false, informatika);
        // master
        Predavac danijel = new Predavac(32L, "Danijel", "Radakovic", false, false, informatika);
        Predavac matijevic = new Predavac(33L, "Milica", "Matijevic", false, false, informatika);
        Predavac ceca = new Predavac(34L, "Svetlana", "Antesevic", false, false, informatika);

        StudentskaGrupa g1_1 = new StudentskaGrupa("1", 1, "L", 16, siit);
        StudentskaGrupa g1_2 = new StudentskaGrupa("2", 1, "L", 16, siit);
        StudentskaGrupa g1_3 = new StudentskaGrupa("3", 1, "L", 16, siit);
        StudentskaGrupa g1_4 = new StudentskaGrupa("4", 1, "L", 16, siit);
        StudentskaGrupa g1_5 = new StudentskaGrupa("5", 1, "L", 16, siit);
        StudentskaGrupa g2_1 = new StudentskaGrupa("1", 2, "L", 16, siit);
        StudentskaGrupa g2_2 = new StudentskaGrupa("2", 2, "L", 16, siit);
        StudentskaGrupa g2_3 = new StudentskaGrupa("3", 2, "L", 16, siit);
        StudentskaGrupa g2_4 = new StudentskaGrupa("4", 2, "L", 16, siit);
        StudentskaGrupa g2_5 = new StudentskaGrupa("5", 2, "L", 16, siit);
        StudentskaGrupa g3_1 = new StudentskaGrupa("1", 3, "L", 16, siit);
        StudentskaGrupa g3_2 = new StudentskaGrupa("2", 3, "L", 16, siit);
        StudentskaGrupa g3_3 = new StudentskaGrupa("3", 3, "L", 16, siit);
        StudentskaGrupa g3_4 = new StudentskaGrupa("4", 3, "L", 16, siit);
        StudentskaGrupa g3_5 = new StudentskaGrupa("5", 3, "L", 16, siit);
        StudentskaGrupa g4_1 = new StudentskaGrupa("1", 4, "L", 16, siit);
        StudentskaGrupa g4_2 = new StudentskaGrupa("2", 4, "L", 16, siit);
        StudentskaGrupa g4_3 = new StudentskaGrupa("3", 4, "L", 16, siit);
        StudentskaGrupa g4_4 = new StudentskaGrupa("4", 4, "L", 16, siit);
        StudentskaGrupa g4_5 = new StudentskaGrupa("5", 4, "L", 16, siit);
        // master
        StudentskaGrupa gm_1 = new StudentskaGrupa("mas 1 gr", 4, "L", 16, siit);
        StudentskaGrupa gm_2 = new StudentskaGrupa("mas 2 gr", 4, "L", 1, siit); // 9 studenatana inace
        StudentskaGrupa gm_3 = new StudentskaGrupa("mas 3 gr", 4, "L", 14, siit);
        StudentskaGrupa gm_4 = new StudentskaGrupa("mas 4 gr", 4, "L", 1, siit);
        StudentskaGrupa gm_5 = new StudentskaGrupa("mas 5 gr", 4, "L", 1, siit);

        // profesori
        List<Predavac> predavaci = new ArrayList<>();
        predavaci.add(zaric);
        predavaci.add(minja);
        predavaci.add(zeljko);
        predavaci.add(branko);
        predavaci.add(goca);
        predavaci.add(luburic);
        predavaci.add(gostojic);
        predavaci.add(sladic);
        predavaci.add(koca);
        predavaci.add(slivka);
        predavaci.add(dejanovic);
        predavaci.add(sinisa);
        // asistenti
        predavaci.add(eva);
        predavaci.add(mitar);
        predavaci.add(tamara);
        predavaci.add(alek);
        predavaci.add(ivan);
        predavaci.add(mina);
        predavaci.add(marko);
        predavaci.add(balsa);
        predavaci.add(veljko);
        predavaci.add(dragan);
        predavaci.add(bane);
        predavaci.add(dusan);
        predavaci.add(aleksandar);
        predavaci.add(budjen);
        predavaci.add(katarina);
        predavaci.add(andjela);
        predavaci.add(milica);
        predavaci.add(marija);
        predavaci.add(indjic);
        // master
        predavaci.add(danijel);
        predavaci.add(matijevic);
        predavaci.add(ceca);

        List<StudentskaGrupa> grupe = new ArrayList<>();
        grupe.add(g1_1);
        grupe.add(g1_2);
        grupe.add(g1_3);
        grupe.add(g1_4);
        grupe.add(g1_5);
        grupe.add(g2_1);
        grupe.add(g2_2);
        grupe.add(g2_3);
        grupe.add(g2_4);
        grupe.add(g2_5);
        grupe.add(g3_1);
        grupe.add(g3_2);
        grupe.add(g3_3);
        grupe.add(g3_4);
        grupe.add(g3_5);
        grupe.add(g4_1);
        grupe.add(g4_2);
        grupe.add(g4_3);
        grupe.add(g4_4);
        grupe.add(g4_5);
        // master
        grupe.add(gm_1);
        grupe.add(gm_2);
        grupe.add(gm_3);
        grupe.add(gm_4);
        grupe.add(gm_5);

        List<Meeting> meetings = new ArrayList<>();
        List<MeetingAssignment> meetingAssignments = new ArrayList<>();
        // vezbe
        // 1. godina
        // vezbe sve 1:30 = 90 = 6 (+1)
        Meeting predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, zeljko, mreze, List.of(g1_1, g1_2, g1_3, g1_4, g1_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        Meeting meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, eva, mreze, List.of(g1_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, eva, mreze, List.of(g1_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, eva, mreze, List.of(g1_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, eva, mreze, List.of(g1_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, eva, mreze, List.of(g1_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, zaric, wd, List.of(g1_1, g1_2, g1_3, g1_4, g1_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mitar, wd, List.of(g1_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mitar, wd, List.of(g1_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mitar, wd, List.of(g1_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mitar, wd, List.of(g1_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mitar, wd, List.of(g1_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, branko, asp, List.of(g1_1, g1_2, g1_3, g1_4, g1_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, tamara, asp, List.of(g1_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, tamara, asp, List.of(g1_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, tamara, asp, List.of(g1_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, tamara, asp, List.of(g1_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, tamara, asp, List.of(g1_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, minja, oop, List.of(g1_1, g1_2, g1_3, g1_4, g1_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, ivan, oop, List.of(g1_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, alek, oop, List.of(g1_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, alek, oop, List.of(g1_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, alek, oop, List.of(g1_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, alek, oop, List.of(g1_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        // 2. godina
        // vezbe sve 1:30 = 90 = 6 (+1) ili 2:30 = 150 = 10 (+1)
        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, goca, sims, List.of(g2_1, g2_2, g2_3, g2_4, g2_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mina, sims, List.of(g2_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mina, sims, List.of(g2_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mina, sims, List.of(g2_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marko, sims, List.of(g2_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mina, sims, List.of(g2_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, luburic, usi, List.of(g2_1, g2_2, g2_3, g2_4, g2_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, balsa, usi, List.of(g2_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, balsa, usi, List.of(g2_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, balsa, usi, List.of(g2_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, balsa, usi, List.of(g2_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, balsa, usi, List.of(g2_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, gostojic, ui, List.of(g2_1, g2_2, g2_3, g2_4, g2_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, veljko, ui, List.of(g2_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, veljko, ui, List.of(g2_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, veljko, ui, List.of(g2_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, veljko, ui, List.of(g2_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, veljko, ui, List.of(g2_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        // 3. godina
        // vezbe sve 2:30 = 150 = 10 (+1) ili 3:15 = 195 = 13 (+1)
        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, sladic, mrs, List.of(g3_1, g3_2, g3_3, g3_4, g3_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marko, mrs, List.of(g3_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marko, mrs, List.of(g3_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, andjela, mrs, List.of(g3_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marko, mrs, List.of(g3_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marko, mrs, List.of(g3_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, koca, List.of(slivka), ori, List.of(g3_1, g3_2, g3_3, g3_4, g3_5));
        meetings.add(predavanje);
        // DVA ORI PREDAVANJA
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, bane, ori, List.of(g3_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, aleksandar, ori, List.of(g3_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, milica, ori, List.of(g3_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, bane, ori, List.of(g3_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, milica, ori, List.of(g3_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, slivka, pigkut, List.of(g3_1, g3_2, g3_3, g3_4, g3_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, pigkut, List.of(g3_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, pigkut, List.of(g3_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, pigkut, List.of(g3_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, pigkut, List.of(g3_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, katarina, pigkut, List.of(g3_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, branko, isa, List.of(g3_1, g3_2, g3_3, g3_4, g3_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, budjen, isa, List.of(g3_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, budjen, isa, List.of(g3_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, budjen, isa, List.of(g3_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dusan, isa, List.of(g3_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, budjen, isa, List.of(g3_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        // 4. godina
        // vezbe sve 2:30 = 150 = 10 (+1) ili 3:15 = 195 = 13 (+1)
        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, sladic, bsep, List.of(g4_1, g4_2, g4_3, g4_4, g4_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marija, bsep, List.of(g4_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marija, bsep, List.of(g4_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marija, bsep, List.of(g4_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marija, bsep, List.of(g4_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, marija, bsep, List.of(g4_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, dejanovic, ntp, List.of(g4_1, g4_2, g4_3, g4_4, g4_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, indjic, ntp, List.of(g4_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, indjic, ntp, List.of(g4_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, indjic, ntp, List.of(g4_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, indjic, ntp, List.of(g4_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, indjic, ntp, List.of(g4_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, slivka, ml, List.of(g4_1, g4_2, g4_3, g4_4, g4_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, ml, List.of(g4_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, ml, List.of(g4_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, ml, List.of(g4_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, ml, List.of(g4_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, dragan, ml, List.of(g4_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, sinisa, sbnz, List.of(g4_1, g4_2, g4_3, g4_4, g4_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, andjela, sbnz, List.of(g4_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, andjela, sbnz, List.of(g4_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, andjela, sbnz, List.of(g4_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, mina, sbnz, List.of(g4_4));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, andjela, sbnz, List.of(g4_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        // master
        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, slivka, ntri, List.of(gm_3));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, slivka, ntri, List.of(gm_3));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        // VEZBE NA KOJIMA SE MENJAJU GRUPE SVAKE DRUGE NEDELJE ILI MENJAJU DELOVI GRUPE???
        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, zaric, devops, List.of(gm_1, gm_2, gm_3, gm_4, gm_5));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, danijel, devops, List.of(gm_1, gm_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, danijel, devops, List.of(gm_3, gm_4, gm_5));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));

        predavanje = new Meeting(TipProstorije.AUD, MeetingType.PRED, gostojic, udf, List.of(gm_1, gm_2));
        meetings.add(predavanje);
        meetingAssignments.add(new MeetingAssignment(predavanje));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, ceca, udf, List.of(gm_1));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));
        meeting = new Meeting(TipProstorije.RAC, MeetingType.RAC, matijevic, udf, List.of(gm_2));
        meetings.add(meeting);
        meetingAssignments.add(new MeetingAssignment(meeting));


        List<Dan> dani = new ArrayList<>();
        List<TimeGrain> timeGrains = new ArrayList<>();
        // pon-pet = 5
        int dayNum = 5;
        // do 17:00 (poslednji grain 16:45-17:00 je pauza) = 32
        // do 17:15 (poslednji grain 17:00-17:15 je pauza) = 33 -> tacno sati koliko nam treba
        // do 17:30 (poslednji grain 17:15-17:30 je pauza) = 34 -> 170h, treba nam 165h
        // do 17:45 (poslednji grain 17:30-17:45 je pauza) = 35 -> 175h
        // do 18:15 (poslednji grain 18:00-18:15 je pauza) = 37
        // do 18:45 (poslednji grain 18:30-18:45 je pauza) = 39
        // do 19:15 (poslednji grain 19:00-19:15 je pauza) = 41
        // do 20:15 (poslednji grain 20:00-20:15 je pauza) = 45
        // od 8:00 do 20:15 = 49
        int grainNum = 45;
        for (int i = 0; i < dayNum; i++) {
            Dan dan = new Dan(i);
            dani.add(dan);
            int start = 9 * 60; // 09:00
            int current;
            for (int j = 0; j < grainNum; j++) {
                current = start + j * 15;
                // grain indeks je unique = i*len(j)+j
                timeGrains.add(new TimeGrain(i * grainNum + j, current, dan));
            }
        }

        MeetingAssignment meetingAssignment = meetingAssignments.iterator().next();
        meetingAssignment.setStartingTimeGrain(timeGrains.iterator().next());
        meetingAssignment.setProstorija(prostorije.iterator().next());

        return new MeetingSchedule(1L, programi, meetings, dani, timeGrains, prostorije, predavaci, grupe, meetingAssignments, Semestar.Z);
    }

}
