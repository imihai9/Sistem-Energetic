# Proiect Energy System Etapa 2

## Despre

Proiectare Orientata pe Obiecte, Seriile CA, CD
2020-2021

<https://ocw.cs.pub.ro/courses/poo-ca-cd/arhiva/teme/2020/proiect/etapa2>

Student: Ionescu Mihai, 325CD

## Rulare teste

Clasa Test#main
  * ruleaza solutia pe testele din checker/, comparand rezultatele cu cele de referinta
  * ruleaza checkstyle

Detalii despre teste: checker/README

Biblioteci necesare pentru implementare:
* Jackson Core 
* Jackson Databind 
* Jackson Annotations

Tutorial Jackson JSON: 
<https://ocw.cs.pub.ro/courses/poo-ca-cd/laboratoare/tutorial-json-jackson>

## Implementare

### Entitati
- **Simulation** contine logica simularii unei runde
- **Main** - entry point; initiaza citirea, simularea si scrie la final in fisierele de output
- **Input** contine listele cu datele despre entitati
- **InputLoader** contine logica citirii din fisier
- Pachetul **fileio/input/** contine clasele in care se incarca initial datele, obiectele propriu-zise din **entities/**
 fiind create ulterior pe baza acestora, folosind factory-urile din **factories/**.
- Pachetul **entities/** contine entitatile care participa la simulare + clase adiacente
(ex. **MonthlyStat** - statistici lunare despre distribuitori, tinute de Producers)
- Pachetul **strategies/** contine cele trei strategii
- Pachetul **updates/** - clase care retin update-urile lunare
- Pachetul **utils/** - metode utile, constante

### Flow

Pentru fiecare runda, se apeleaza Simulation, care tine liste de Consumatori, Distribuitori
si Producatori ordonate in functie de id si filtrate pentru a include doar entitatile care
nu sunt in faliment.

Dupa, au loc urmatorii pasi:
- In prima runda, distribuitorii isi aleg producatorii pe baza strategiilor fiecaruia, apoi
isi calculeaza costurile de productie si costurile contractelor.
- Se citesc update-urile lunare. Pentru update-urile producatorilor, acestia seteaza un flag
pentru distribuitorii aferenti lor.
- Se calculeaza costul contractelor distribuitorilor
- Se scot din joc distribuitorii falimentari, anulandu-se contractele aferente
- Consumatorii primesc salariile
- Se efectueaza platile consumatori->distribuitori, cu verificarea amanarilor si a
falimentului consumatorilor
- Se scad cu 1 lunile ramase din fiecare contract
- Distribuitorii cu flag-ul setat isi aleg din nou producatorii
- Distribuitorii isi recalculeaza costul de productie.
- Se adauga in log-ul producatorilor distribuitorii din luna curenta.

### Design patterns

- **Singleton**
    - Folosit de InputLoader (acesta nu contine o stare, ci doar logica citirii)
- **Factory**
    - Folosit de clasele din **factories/**, pentru a construi obiecte Consumers, Distributors, Producers si strategii
de alegere a producatorilor
- **Strategy**
    - Folosit pentru alegerea producatorilor, in functie de preferintele fiecarui distribuitor
- **Observer**
    - Producers = Observables, Distributors = Observers; cand are loc o modificare de putere generata per distribuitor
in cadrul unui producator, toti distribuitorii aferenti acestuia sunt notificati.
