<body>
  <h1>UdpServer</h1>
  <p>Der UdpServer ist ein Teil eines Audit-Log-Systems, das über UDP kommuniziert. Dieser Server dient dazu, Audit-Log-Nachrichten von Clients zu empfangen, zu verarbeiten und in einer CSV-Datei für spätere Analysen zu protokollieren.</p>
  
  <h2>Funktionen:</h2>
  <ul>
      <li><strong>Server-Socket-Erstellung:</strong> Initialisiert einen UDP-Server-Socket, der auf eingehende Verbindungen wartet.</li>
      <li><strong>Verbindungshandling:</strong> Wartet auf die Verbindung eines Clients und verarbeitet die empfangenen Audit-Logs.</li>
      <li><strong>Audit-Log-Verarbeitung:</strong> Nimmt Audit-Logs entgegen, extrahiert Informationen und druckt diese auf der Konsole aus.</li>
      <li><strong>CSV-Protokollierung:</strong> Schreibt die empfangenen Audit-Log-Informationen abhängig vom Typ der PDU in eine CSV-Datei.</li>
      <li><strong>Ressourcenmanagement:</strong> Schließt die Verbindung und den Dateischreiber ordnungsgemäß.</li>
  </ul>
  
  <h2>Information:</h2>
  <p>Aus Datenschutzgründen sind spezifische Implementierungsdetails und Teile des Codes, in dieser Bereitstellung nicht enthalten. Der Code konzentriert sich auf die grundlegende Infrastruktur und Mechanismen zur Verarbeitung von Audit-Logs über UDP.</p>
  
</body>
</html>
