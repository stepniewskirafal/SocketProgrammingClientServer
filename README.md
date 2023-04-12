Aplikacja client / serwer operująca na socketach.

Serwer ma obsługiwać następujące polecenia (i zwraca odpowiedź w JSONie).

"uptime" - zwraca czas życia serwera
"info" - zwraca numer wersji serwera, datę jego utworzenia
"help" - zwraca listę dostępnych komend z krótkim opisem (tych komend, z tej listy, którą właśnie czytasz, czyli inaczej, komend realizowanych przez serwer)
"stop" - zatrzymuje jednocześnie serwer i klienta