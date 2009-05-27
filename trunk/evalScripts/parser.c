// Dateiname      : parser.c
// Letzte Änderung: 06. September 2006 durch Edgar Binder
// Autoren        : Edgar Binder
//
// Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
// Siehe Datei "license.txt" für Hinweise zur Lizenz.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define MAX_LINE_SIZE 1024
#define MAX_PROBLEMS 256
#define MAX_RUNS 1024 * 128
#define MIN_PRECISION 0.000001

typedef char* string;

string problem_names[MAX_PROBLEMS];
float  problem_costs[MAX_PROBLEMS][MAX_RUNS];
int    problem_runs[MAX_PROBLEMS];
int    problem_times[MAX_PROBLEMS][MAX_RUNS];
int    problems_num = 0;

// ********************************************************
// * gibt eine Fehlermeldung aus und beendet das Programm *
// ********************************************************

void error(string s) {
    fprintf(stderr, "%s\n", s);
    exit(1);    
}

// *********************************************************
// * gibt fuer bekannten Problemen deren Index zurueck und *
// * vergibt neue Indizes fuer unbekannte Probleme         *
// *********************************************************

int get_problem_index(string name) {
    int i;
    
    for (i = 0; i < problems_num; i++) {
        if (strcmp(problem_names[i], name) == 0) {
            return i;
        }
    }

    if (problems_num >= MAX_PROBLEMS) {
        error("too many problems!");
    }
    
    problem_names[problems_num] = (string) strdup(name);
    problem_runs[problems_num] = 0;
    problems_num++;
    
    return problems_num - 1;
}

// *****************************************************************
// * fuegt einen neuen Messergebnis in der bisherigen Sammlung ein *
// *****************************************************************

void add_test_result(string name, float cost, int time) {
    int index = get_problem_index(name);
    int runs = problem_runs[index];
    
    if (runs >= MAX_RUNS) {
        error("too many runs!");
    }
    
    problem_costs[index][runs] = cost;
    problem_times[index][runs] = time;
    problem_runs[index] = runs + 1;
}

// *******************************************************************
// * prueft ob der naechste Eingabetoken den erwarteten Wert besitzt *
// *******************************************************************

void check_next_token(FILE* stream, string expected) { 
    char token[MAX_LINE_SIZE];    
    fscanf(stream, "%s\n", token); 
    if (strcmp(token, expected) != 0) {
      fprintf(stderr, "expected token: \"%s\", read token: \"%s\"!\n", expected, token);
      exit(1);
    }    
}

// *******************************
// * verarbeitet die Texteingabe *
// *******************************

void parse_file(string filename) {
    FILE* stream = NULL;
    char token[MAX_LINE_SIZE];
    char problem_name[MAX_LINE_SIZE];
    float problem_cost;
    int problem_time;
    float total_time;
    float total_cost;
    
    fprintf(stderr, "parsing %s...", filename);
  
    stream = fopen(filename, "rt"); // Datei oeffnen in Lese- und Textmodus

    // falls die Datei nicht geoeffnet werden konnte, beende das Programm    
    if (stream == NULL) {
        error("file not found!");
    }
    
    while (!feof(stream)) {
        fscanf(stream, "%s", token);  
                      
        if (strcmp(token, "Problem:") == 0) {

            // liest den Namen des Problems ein
            fscanf(stream, "%s", token);
        
            // dieser enthaehlt noch ein Semikolon am Ende, 
            // das von folgender Zeile entfernt wird.
            token[strlen(token) - 1] = 0; 
            
            // der Name wird nun im String problem_name gespeichert
            strcpy(problem_name, token);

            // es wird hier der Token "Kosten" erwartet
            check_next_token(stream, "Kosten");
                
            // es wird hier der Token "=" erwartet
            check_next_token(stream, "=");
                        
            // hier wird eine Gleitkommazahl erwartet
            fscanf(stream, "%f", &problem_cost);
            
            // es wird hier der Token "," erwartet
            check_next_token(stream, ",");

            // es wird hier der Token "Zeit" erwartet
            check_next_token(stream, "Zeit");
                
            // es wird hier der Token "=" erwartet
            check_next_token(stream, "=");
                        
            // hier wird eine Zeitangabe in Millisekunden erwartet
            fscanf(stream, "%i", &problem_time);

            // es wird hier der Token "ms" erwartet
            check_next_token(stream, "ms");

            // fuege das neue Testergebnis zur Problemensammlung hinzu
            add_test_result(problem_name, problem_cost, problem_time);

        }
          
        if (strcmp(token, "Gesamtkosten") == 0) {

            // es wird hier "=" erwartet 
            check_next_token(stream, "=");

            // es wird hier eine Gleitkommazahl erwartet, die Gesamtkosten
            fscanf(stream, "%f\n", &total_cost);  

            // die naechste Zeile nach den Gesamtkosten sollte als erster
            // den Token "Gesamtzeit" enthalten
            check_next_token(stream, "Gesamtzeit");
            
            // gefolgt vom Token "="
            check_next_token(stream, "=");

            // hier wird die Zeitangabe in Sekunden erwartet
            fscanf(stream, "%f", &total_time);
            
            // der Token "s" wird nun erwartet
            check_next_token(stream, "s");

            // fuege das neue Testergebnis zur Problemensammlung hinzu            
            add_test_result("Total costs", total_cost, (int)(total_time * 1000));                        
        }
    }
 
    fprintf(stderr, "done\n");
}

// ******************************************************************************
// * ergaenzt ggf. die uebergebene Zahl um eine fuehrende Null und gibt sie aus *
// ******************************************************************************

void display_two_digit(int a) {
    if (a < 10) {
        printf("0%i", a);
    } else {
        printf("%i", a);
    }
}
// ********************************
// * gibt die vergangene Zeit aus *
// ********************************

void display_time(int time) {
    if (time < 1000) {
        printf("%i ms\n", time);
    } else if (time < 60 * 1000) {
        printf("%.3f s\n", (float)time / 1000);
    } else {
        display_two_digit(time / (60 * 60 * 1000));
        printf(":");
        display_two_digit((time / (60 * 1000)) % 60);
        printf(":");
        display_two_digit((time / 1000) % 60);
        printf(".");
        display_two_digit((time / 10) % 100);
        printf("\n");
    }
}

// **************************************************************
// *  gibt die verarbeiteten Ergebnissen auf dem Bildschirm aus *
// **************************************************************

void show_results() {
    int i, j, repetitions, min_time, max_time, time;
    float sum_time, mean, mean_time, quad_sum, quad_sum_time;
    float sum_costs, last_costs, std_deviation;
    
    for (i = 0; i < problems_num; i++) {
        printf("\n--------------------------------------------------------------\n\n");
        printf("Name: %s\n", problem_names[i]);
        printf("Runs: %i\n\n", problem_runs[i]);
        printf("Costs\n");
        
        repetitions = 0;
        last_costs = problem_costs[i][0];
        
        sum_costs = 0;
        sum_time = 0;
        min_time = 0x7FFFFFFF;
        max_time = 0;
        
        for (j = 0; j < problem_runs[i]; j++) {
            
            sum_costs += problem_costs[i][j];
            
            time = problem_times[i][j];
            sum_time += time;
            
            if (min_time > time) {
                min_time = time;
            }
            
            if (max_time < time) {
                max_time = time;
            }
                    
            if (problem_costs[i][j] == last_costs) {
                repetitions++;
            } else {
                if (last_costs - floor(last_costs) < MIN_PRECISION) {
                  printf("%i -> %ix\n", (int)last_costs, repetitions);
                } else {
                  printf("%f -> %ix\n", last_costs, repetitions);
                }
                last_costs = problem_costs[i][j];
                repetitions = 1;                
            }            
        }        
        
        mean = sum_costs / (float) problem_runs[i];
        quad_sum = 0;

        mean_time = (float) sum_time / (float) problem_runs[i];
        quad_sum_time = 0;
        
        for (j = 0; j < problem_runs[i]; j++) {
            quad_sum += (problem_costs[i][j] - mean) * (problem_costs[i][j] - mean);
            quad_sum_time += (problem_times[i][j] / 1000.0 - mean) * (problem_times[i][j] / 1000.0 - mean);
        }
        
        if (last_costs - floor(last_costs) < MIN_PRECISION) {
            printf("%i -> %ix\n", (int)last_costs, repetitions);
        } else {
            printf("%f -> %ix\n", last_costs, repetitions);
        }
                
        //printf("\nMin time       : %fs\n", (float) min_time / 1000.0);        
        //printf("Average time   : %fs\n", mean_time);
        //printf("Max time       : %fs\n", (float) max_time / 1000.0);

        printf("\nMin time       : ");
        display_time(min_time);        
        
        printf("Average time   : ");
        display_time((int)(mean_time));
        
        printf("Max time       : ");
        display_time(max_time);

        printf("\nAverage costs  : %f\n", mean);
        
        if (problem_runs[i] > 1) {
            std_deviation = sqrt(quad_sum / (float) (problem_runs[i] - 1));
        } else {
            std_deviation = 0;
        }
        printf("Std. deviation : %f\n", std_deviation);
    }
}

// *****************************
// * sortiert das Eingabearray *
// *****************************

void sort_array(float* values, int num) {
    int i, j, tmp;
    
    for (i = 0; i < num; i++) {
        for (j = i + 1; j < num; j++) {
            if (values[i] > values[j]) {
                tmp = values[i];
                values[i] = values[j]; 
                values[j] = tmp;
            }
        }
    }
}

// ************************************************
// * sortiert die gemessenen Kosten der Problemen *
// ************************************************

void sort_results() {
    int i;
    
    for (i = 0; i < problems_num; i++) {
        sort_array(problem_costs[i], problem_runs[i]);
    }
}

// ********************************************************************************
// * liest die Messergebnisse ein und gibt sie in einer uebersichtlichen Form aus *
// ********************************************************************************

int main(int argc, char *argv[])
{
    int i;
    
    if (argc == 1) {
        fprintf(stderr, "Syntax: parser outputfile1 [outputfile2 outputfile3 ...]\n");
        exit(0);
    }
    
    for (i = 1; i < argc; i++) {
        parse_file(argv[i]);
    }
    
    sort_results();
    show_results();
}

