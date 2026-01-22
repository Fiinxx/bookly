# Bookly - Portfolio 03 Project

Bookly ist ein Backend-System zur Verwaltung von Büchern, Benutzern und Bewertungen. Dieses Projekt wurde im Rahmen des Moduls **Backend Systems** (Portfolio 03) entwickelt und implementiert eine saubere Trennung von Belangen durch die hexagonale Architektur.

##  Hexagonale Architektur (Ports & Adapters)

Das System folgt der hexagonalen Architektur, um die Geschäftslogik von externen Einflüssen wie Datenbanken oder APIs zu isolieren:

* **Domain Layer**: Beinhaltet die Kernmodelle (`Book`, `User`, `Rating`) und die Geschäftslogik.
* **Inbound Ports**: Interfaces, die definieren, wie die Anwendung genutzt werden kann (z. B. `LoadBookUseCase`, `CreateRatingUseCase`).
* **Outbound Ports**: Interfaces für externe Abhängigkeiten (z. B. `PersistBookPort`, `FetchBookDetailsPort`).
* **Inbound Adapter**: REST-Controller, die HTTP-Anfragen in Use-Case-Aufrufe umwandeln.
* **Outbound Adapter**: Implementierungen für Persistenz (JPA/Panache) und externe REST-Clients (OpenLibrary).



##  Voraussetzungen

* **Java**: OpenJDK 24 (oder kompatibel)
* **Build-Tool**: Maven 3.9.x
* **Docker**: Erforderlich für die Ausführung

## Integrationstests starten 

Gemäß den Prüfungsanforderungen können alle Integrationstests mit einem einzigen Befehl ausgeführt werden. Quarkus fährt hierbei automatisch die erforderliche Test-Infrastruktur (inkl. Datenbank) hoch.

Führen Sie im Projekt-Root-Verzeichnis folgenden Befehl aus:

```shell
./mvnw verify