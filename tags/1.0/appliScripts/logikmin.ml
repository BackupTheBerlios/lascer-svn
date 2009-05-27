(*
  Dateiname      : logikmin.ml
  Letzte Änderung: 31. Aug 2006 durch Dietmar Lippold
  Autoren        : Dietmar Lippold
 
  Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
  Siehe Datei "license.txt" für Hinweise zur Lizenz.
*)

                (* Funktionen zur logischen Minimierung. *)


type term =
  | PVar of char
  | NVar of char
  | AND of term list
  | OR  of term list;;


(*** Erzeugung mehrstufiger Terme. ***)

(* Liefer zu einer Liste von Term-Listen eine Liste der daraus erzeugten
   AND-Terme. *)
let rec and_terme = function
  | [] -> []
  | termliste :: restlisten -> AND(termliste) :: and_terme(restlisten);;

(* Liefer zu einer Liste von Term-Listen eine Liste der daraus erzeugten
   OR-Terme. *)
let rec or_terme = function
  | [] -> []
  | termliste :: restlisten -> OR(termliste) :: or_terme(restlisten);;


(* Liefert eine Liste von Listen mit jeweils einer positiven Variablen. *)
let rec pvar_terme = function
  | [] -> []
  | hvar :: tvar -> [PVar(hvar)] :: pvar_terme(tvar);;

(* Liefert eine Liste von Listen mit jeweils einer negativen Variablen. *)
let rec nvar_terme = function
  | [] -> []
  | hvar :: tvar -> [NVar(hvar)] :: nvar_terme(tvar);;


(* Fügt ein Element vorne an eine Liste an. *)
let rec cons element liste = element :: liste;;

(* Hängt zwei Listen zusammen. *)
let rec append liste1 liste2 = liste1 @ liste2;;


(* Fügt jedes Element einer Liste vorne an jede Liste einer Liste von
   Listen. *)
let rec term_list_kombi(kopf_liste, rest_listen) = match kopf_liste with
  | [] -> []
  | h :: t -> List.map(cons(h))(rest_listen) @ term_list_kombi(t, rest_listen);;

(* Hängt jede Liste einer Liste von Listen vor jede andere Liste einer
   Liste von Listen. *)
let rec list_list_kombi(kopf_listen, rest_listen) = match kopf_listen with
  | [] -> []
  | h :: t -> List.map(append(h))(rest_listen) @ list_list_kombi(t, rest_listen);;


(* Liefert eine Liste von Term-Listen, wobei jede Term-Liste die Komplexitaet
   komplex hat. *)
let rec term_listen(komplex, variablen) =
  if (komplex == 0) then
    []
  else if (komplex == 1) then
    pvar_terme(variablen) @ nvar_terme(variablen)
  else
    komb_term_listen(komplex, 0, variablen)

and

  (* Liefert eine Liste von Term-Listen, wobei jede Term-Liste die
     Komplexitaet (komplex1 + komplex2) hat. *)
  komb_term_listen(komplex1, komplex2, variablen) =
    if (komplex1 <= komplex2) then
      []
    else
      term_list_kombi(multi_terme(komplex1, variablen),
                      term_listen(komplex2, variablen))
      @ list_list_kombi(term_listen(komplex1 - 1, variablen),
                        term_listen(komplex2 + 1, variablen))
      @ komb_term_listen(komplex1 - 1, komplex2 + 1, variablen)

and

  (* Liefert eine Liste von Termen mit Komplexitaet komplex. *)
  multi_terme(komplex, variablen) =
    if (komplex == 0) then
      []
    else
      let termlisten = term_listen(komplex - 1, variablen) in
      (and_terme(termlisten) @ or_terme(termlisten));;


(*** Erzeugung zweistufiger Terme. ***)

(* Lifert zu einem Wert eine Liste mit diesem Wert. *)
let make_list(wert) = [wert];;

(* Liefert zu einer Termliste einen AND-Term. *)
let make_and(termliste) = AND(termliste);;

(* Liefert zu einer Termliste einen OR-Term. *)
let make_or(termliste) = OR(termliste);;


(* Liefert eine Liste der Variablen. *)
let var_liste(variablen) =
  List.concat(pvar_terme(variablen) @ nvar_terme(variablen));;

(* Liefert eine Liste von Variablen-Listen jeweils mit der Länge komplex. *)
let rec var_listen(komplex, variablen) =
  if (komplex == 0) then
    []
  else if (komplex == 1) then
    pvar_terme(variablen) @ nvar_terme(variablen)
  else
    term_list_kombi(var_liste(variablen),
                    var_listen(komplex - 1, variablen));;


(* Liefert eine Liste von OR-Termen oder Variablen. *)
let or_terme(komplex, variablen) =
  if (komplex <= 0) then
    []
  else if (komplex == 1) then
    var_liste(variablen)
  else
    List.map(make_or)(var_listen(komplex - 1, variablen));;

(* Liefert eine Liste von AND-Termen oder Variablen. *)
let and_terme(komplex, variablen) =
  if (komplex <= 0) then
    []
  else if (komplex == 1) then
    var_liste(variablen)
  else
    List.map(make_and)(var_listen(komplex - 1, variablen));;


(* Liefert eine Liste von Listen aus OR-Termen und Variablen, wobei die
   Summe der Komplexitäten der Terme einer Liste (komplex1 + komplex2) ist
   und der erste Term jeder Liste eine Komplexität kleiner oder gleich
   komplex1 hat. *)
let rec or_terme_kombi(komplex1, komplex2, variablen) =
  if (komplex1 < komplex2) then
    []
  else if (komplex2 == 0) then
    List.map(make_list)(or_terme(komplex1, variablen))
    @ or_terme_kombi(komplex1 - 1, komplex2 + 1, variablen)
  else
    term_list_kombi(or_terme(komplex1, variablen),
                    or_terme_kombi(komplex2, 0, variablen))
    @ or_terme_kombi(komplex1 - 1, komplex2 + 1, variablen);;

(* Liefert eine Liste von Listen aus AND-Termen und Variablen, wobei die
   Summe der Komplexitäten der Terme einer Liste (komplex1 + komplex2) ist
   und der erste Term jeder Liste eine Komplexität kleiner oder gleich
   komplex1 hat. *)
let rec and_terme_kombi(komplex1, komplex2, variablen) =
  if (komplex1 < komplex2) then
    []
  else if (komplex2 == 0) then
    List.map(make_list)(and_terme(komplex1, variablen))
    @ and_terme_kombi(komplex1 - 1, komplex2 + 1, variablen)
  else
    term_list_kombi(and_terme(komplex1, variablen),
                    and_terme_kombi(komplex2, 0, variablen))
    @ and_terme_kombi(komplex1 - 1, komplex2 + 1, variablen);;


(* Liefert eine Liste von AND-Termen aus OR-Termen, d.h. eine Liste
   konjunktiver Normalformen. *)
let and_or_terme(komplex, variablen) =
  List.map(make_and)(or_terme_kombi(komplex - 1, 0, variablen));;

(* Liefert eine Liste von OR-Termen aus AND-Termen, d.h. eine Liste
   disjunktiver Normalformen. *)
let or_and_terme(komplex, variablen) =
  List.map(make_or)(and_terme_kombi(komplex - 1, 0, variablen));;


(*** Auswertung von Termen. ***)

(* Liefert den Wert eines Terms. *)
let rec wert(term, assocliste) = match term with
  | PVar(name) -> List.assoc name assocliste
  | NVar(name) -> not(List.assoc name assocliste)
  | AND(termliste) -> (match termliste with
                       | [] -> true
                       | h :: t -> wert(h, assocliste)
                                   && wert(AND(t), assocliste))
  | OR(termliste) -> (match termliste with
                      | [] -> false
                      | h :: t -> wert(h, assocliste)
                                  || wert(OR(t), assocliste));;


(* Wandelt ein ganze Zahl in einen bool Wert. *)
let bool_wert = function
  | 0 -> false
  | _ -> true;;

(* Liefert zu zwei Listen von Zahlen und Variablen eine assoc-Liste. *)
let rec liste_to_assoc(werte, variablen) = match werte with
  | [] -> []
  | h :: t -> (List.hd(variablen), bool_wert(h))
              :: liste_to_assoc(t, List.tl(variablen));;

(* Ermittelt, ob der Term die Tabelle richtig berechnet. *)
let rec erfuellt(term, tabelle, variablen) = match tabelle with
  | [] -> true
  | (werte, resultat) :: t -> wert(term, liste_to_assoc(werte, variablen)) == bool_wert(resultat)
                              && erfuellt(term, t, variablen);;

(* Liefert eine Liste der Terme, die die Tabelle richtig berechnen. *)
let rec erfuellende_terme(termliste, tabelle, variablen) = match termliste with
  | [] -> []
  | h :: t -> if (erfuellt(h, tabelle, variablen)) then
                h :: erfuellende_terme(t, tabelle, variablen)
              else
                erfuellende_terme(t, tabelle, variablen);;


(*** Tests. ***)
let wertetabelle1 =
  [([1; 1; 0; 1; 0], 0);
   ([1; 1; 0; 0; 0], 1);
   ([0; 1; 0; 0; 0], 0);
   ([1; 0; 0; 1; 0], 1)];;

let wertetabelle2 =
  [([1; 1; 0; 1; 0], 0);
   ([1; 1; 0; 0; 0], 1);
   ([0; 1; 0; 0; 0], 0);
   ([1; 0; 0; 1; 0], 1);
   ([0; 1; 0; 0; 0], 1)];;

let variablen = ['a'; 'b';'c';'d';'e'];;

(*
#use "logikmin.ml";;

erfuellende_terme(multi_terme(5, variablen), wertetabelle1, variablen);;

erfuellende_terme(and_or_terme(5, variablen), wertetabelle1, variablen);;

erfuellende_terme(or_and_terme(5, variablen), wertetabelle1, variablen);;
*)

