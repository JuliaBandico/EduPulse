# EduPulse Backend

Spring Boot 3 + H2 (file-based). Needs **Java 17+ to compile**, but you do not have to install it:

- **Windows:** double-click `run-backend.bat` (or run it in a terminal)
- **Linux/macOS:** `./run-backend.sh`

The script uses a JDK if you already have one; otherwise it downloads a portable JDK 17 into `.jdk/`
(no installer, no admin rights, no PATH changes). The API starts on http://localhost:8080.

## Where data is saved
`Backend/data/edupulse.mv.db` (created on first run, survives restarts, each machine has its own copy).
Inspect it at http://localhost:8080/h2-console with JDBC URL `jdbc:h2:file:./data/edupulse`, user `sa`, empty password.
To start fresh, stop the app and delete the `data/` folder.

## Quick save test
```
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"email\":\"a@b.edu\",\"password\":\"password123\",\"firstName\":\"Ana\",\"lastName\":\"Cruz\",\"isTeacher\":true,\"isStudent\":false,\"isAdmin\":false}"
```
Restart the app, then open http://localhost:8080/api/users - the user is still there.

## Running with only a JRE
Compiling needs a JDK, running does not. Someone with a JDK builds once (`mvnw package -DskipTests`) and shares
`target/edupulse-0.0.1-SNAPSHOT.jar`; everyone else runs `java -jar edupulse-0.0.1-SNAPSHOT.jar`.
