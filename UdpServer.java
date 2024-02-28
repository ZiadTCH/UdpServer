package edu.hm.dako.udpauditlogserver;

import edu.hm.dako.common.AuditLogPDU;
import edu.hm.dako.common.AuditLogPduType;
import edu.hm.dako.connection.udp.UdpServerConnection;
import edu.hm.dako.connection.udp.UdpServerSocket;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class UdpServer {
    // Socket zum Empfangen von Verbindungen
    private UdpServerSocket serverSocket = null;

    // Verbindung zum Client
    private static UdpServerConnection con = null;

    // Aktuelle empfangene Audit-Log-PDU
    private static AuditLogPDU auditlogPdu;

    // Schreiber für die CSV-Datei
    private PrintWriter writer;

    /**
     * main Methode zum Starten des Servers
     */
    public static void main(String[] args) {
        System.out.println("Server gestartet");

        // Server-Objekt erstellen
        UdpServer server = new UdpServer();
        try {
            // Server-Socket erstellen
            server.createSocket();
        } catch (Exception e1) {
            throw new RuntimeException();
        }

        // Server-Schleife für die Verbindung und Verarbeitung der Audit-Logs
        while (true) {
            try {
                // Auf Verbindung warten
                server.waitForConnection();

                // Audit-Log-PDU verarbeiten
                server.handleAuditLog();

                // Audit-Log in CSV-Datei schreiben
                server.writeToCSV();

            } catch (Exception e) {
                System.out.println("Exception beim Auditlog handeln oder Verbindung abgebaut");
                // Verbindung schließen und Server beenden
                server.close();
            }
        }
    }


    /**
     * Methode zum Erstellen des Server-Sockets
     * @throws Exception beim Erstellen des Server Sockets
     */
    private void createSocket() throws Exception {
        try {
            serverSocket = new UdpServerSocket(40001, 400000, 400000);
        } catch (Exception e) {
            System.out.println("Exception beim Erstellen des Server-Sockets");
            throw new Exception();
        }
    }


    /**
     * Methode zum Warten auf eine Verbindung vom Client
     * @throws Exception beim Warten auf Verbindungsaufbau zum Client
     */
    private void waitForConnection() throws Exception {
        try {
            con = (UdpServerConnection) serverSocket.accept();
        } catch (Exception e) {
            System.out.println("Exception beim Warten auf Verbindungsaufbau zum Client");
            throw new Exception();
        }
    }


    /**
     * verarbeiten des empfangenen Audit-Log-PDU
     */
    private void handleAuditLog() {
        try {
            // Audit-Log-PDU empfangen
            auditlogPdu = (AuditLogPDU) con.receive();

            if (auditlogPdu != null) {
                String message = auditlogPdu.getMessage();

                if (message != null) {
                    String userName = auditlogPdu.getUserName();
                    long auditTime = auditlogPdu.getAuditTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // Protokollierung der empfangenen Audit-Log-Daten
                    System.out.println("AuditLog-PDU empfangen von " + userName + "\n" +
                            "Chat-Client-Thread = " + auditlogPdu.getClientThreadName() + "\n" +
                            "Worker-Thread = " + auditlogPdu.getServerThreadName() + "\n" +
                            "Message: " + message + "\n" +
                            "Zeitpunkt: " + dateFormat.format(auditTime));

                } else {
                    // Leere Nachricht empfangen
                    System.out.println("-----------------------------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception beim Handling: " + e.getMessage());
        }
    }


    /**
     * Methode zum Schreiben der Audit-Logs in eine CSV-Datei
     */
    private void writeToCSV() {
        try {
            String userName = auditlogPdu.getUserName();
            String message = auditlogPdu.getMessage();
            AuditLogPduType pduType = auditlogPdu.getPduType();

            // Initialisiere den PrintWriter nur, wenn er noch nicht erstellt wurde
            if (writer == null) {
                writer = new PrintWriter(new FileWriter("udpProtokoll.csv", true));
            }

            // Schreibe abhängig vom PDU-Typ in die CSV-Datei
            switch (pduType) {
                case LOGIN_REQUEST:
                    writer.println(userName + " hat sich angemeldet.");
                    break;
                case CHAT_MESSAGE_REQUEST:
                    writer.println(userName + " hat eine Nachricht geschrieben: " + message);
                    break;
                case LOGOUT_REQUEST:
                    writer.println(userName + " hat sich abgemeldet.");
                    break;
                case FINISH_AUDIT_REQUEST:
                    writer.println("Server ist geschlossen.");
                    break;
                default:
                    // Unerwarteter PDU-Typ
                    break;
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Exception beim Schreiben der CSV-Datei");
        }
    }


    /**
     * Schließen der Verbindung und des Schreibers
     */
    private void close() {
        try {
            // Schließe den Schreiber, wenn vorhanden
            if (writer != null) {
                writer.close();
                writer = null; // Setze den Writer auf null, um ihn beim nächsten Schreibvorgang neu zu initialisieren
            }
            // Schließe die Verbindung
            con.close();
            System.out.println("Verbindung geschlossen");
        } catch (Exception e) {
            System.out.println("Exception beim Schließen");
        }
    }
}

