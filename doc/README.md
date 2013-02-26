1. login
2. search
	- box1: search patient code & Name, surname
	result:
	- seznam pacientov ima link na PreOP, OP, Follow UP
	
3. menu
	- add new
	- search (always on top)
	- statistics (v2)
	
4. forms:
	1. add new / edit / view
		- osebni podati + PreOP + truz (truz default skrito)
			on save:
			- generiraj kodo (5 mestno alfanumerišno)
			- prikaži podatke, ki se jih lahko sprinta (PreOP podatki, desno ID (osebni podatki in koda), spodaj skrito truz)
	2. OP
	3. follow UP (1:n)
	
5. users
	- super admin
	- admin (na organizacijsko enoto)
	- user (zdravnik)

---
- seznam pacientov je rezultat search-a, išče se po Code, Name, Surname, ExternalId
- drug service naj vrača podatke o posameznem pacientu

osnovni podatki:
ID
Code (generirana 6 mestna alfanumerična koda, case isensitive)
Name
Surname
BirthDate
ExternalId (Št Kartona)

---
data export: izvoz podatkov v cvs
