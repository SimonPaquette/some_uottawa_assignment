(*** CSI 3120 Assignment 6 ***)
(*** Simon Paquette ***)
(*** 300044038 ***)
(***4.08 with ocaml-top 1.2 ***)
(* If you use the version available from the lab machines via VCL, the
   version is 4.05.0 ***)

(*********************************************)
(* PROBLEM 1: Function-Oriented Organization *)
(*********************************************)

(* Consider the OCaml code below. *)

type fBalance = float
type fInterestRate = float
type fMonthlyPayment = float

type loan =
    | NotePayable of fBalance
    | CreditCard of fBalance * fInterestRate
    | BankLoan of fBalance * fMonthlyPayment

(* Problem 1(a) *)
(* Write a function that takes a list of loans and returns a
   list of balances.  Your function must preserve order.  For
   example, the first element of the result must be the balance of the
   first loan in the input list *)

(* let get_fBalances l =
   or
   let rec get_fBalances l = *)

let rec get_fBalances (l:loan list) : fBalance list =
  match l with
    | [] -> []
    | hd::tl ->
        match hd with
          | NotePayable f -> [f] @ get_fBalances tl
          | CreditCard (f,i) -> [f+.f*.i] @ get_fBalances tl
          | BankLoan (f,m) -> [f+.f*.m] @ get_fBalances tl
;;

let accounts = [NotePayable 100.; CreditCard (50.,0.10); BankLoan (200.,0.)];;
let q1b = get_fBalances accounts;;



(* Problem 1(b)  *)
(* Write some test code: Create a list containing 3 loan accounts (one
   of each kind).  Set the monthly payment for the bank loan
   to 0. Apply your function from part 1(a) to your list of accounts *)


(*******************************************)
(* PROBLEM 2: Object-Oriented Organization *)
(*******************************************)

(* Your code for parts 2(a) and (b) go here *)

exception Deficit of float
class note_payable = object
  val mutable fBalance : float = 0.0
  method get_balance : float = fBalance
  method set_balance f = fBalance <- f 
  method borrow f = fBalance <- fBalance +. f
  method payback f = 
    if f < fBalance then
      fBalance <- fBalance +. f
    else
      raise (Deficit fBalance) 
  method to_loan = NotePayable fBalance
end

class credit_card = object
  inherit note_payable as super
  val mutable fInterestRate : float = 0.20
  method get_balance = super#get_balance +. super#get_balance*.fInterestRate
  method set_interest i = fInterestRate <- i
  method get_interest = fInterestRate
  method add_interest = super#set_balance (super#get_balance +. super#get_balance*.fInterestRate)
  method to_loan = CreditCard (super#get_balance, fInterestRate)
end

class bank_loan = object
  inherit note_payable as super
  val mutable fMonthlyPayment : float = 0.0
  method get_balance = super#get_balance +. super#get_balance*.fMonthlyPayment
  method set_monthly m = fMonthlyPayment <- m
  method get_monthly = fMonthlyPayment
  method borrow f = 
    super#set_balance (super#get_balance +. f);
    fMonthlyPayment <- (fMonthlyPayment +. 0.1)
  method payback_monthly_amount = 
    super#set_balance (super#get_balance -. fMonthlyPayment)
  method to_loan = BankLoan (super#get_balance, fMonthlyPayment)
end


(* Problem 2(a)  *)
(* Implement an inheritance hierarchy of loans (an
   object-oriented version of the data type in Problem 1). The
   credit_card and bank_loan classes should be subclasses
   of the note_payable class.  The arguments to the NotePayable, CreditCard, and
   BankLoan constructors of the loan data type should become
   instance variables.  The initial values should be 0.0 for the
   balance, 0.2 (representing 20%) for the interest rate, and 0.0 for
   the monthly payment.  Define a method called get_balance that
   returns the balance amount.

   Use the following programming conventions.
   - Do not use abstract classes.
   - The instance variables and methods should go in the highest class
     possible in the hierarchy to maximize inheritance.  Only override
     methods when necessary.  (In this hierarchy credit cards are
     the only kind of loan with an interest rate and bank
     loans are the only kind of loan with a monthly payment.)
   - In a subclass, do not use instance variables of the super class
     directly.  For example, if the implementation of a method in
     credit_card needs to access the balance amount, then it must
     call "get_balance".

   Implement methods called "borrow" and "payback" that take one
   argument, the amount to add to (borrow) or subtract from (payback)
   the balance.  If the amount of the payback is more than the balance,
   raise an exception that takes one argument. The data returned
   when this exception is raised should be the loan balance.

   Add methods in credit_card to get and set the interest rate.
   Also add an "add_interest" method that modifies the balance by
   adding interest to the balance using the interest rate.

   In the bank_loan class, override the borrow method so that
   it also increases the monthly payment by 10% (0.1) of the borrowed
   amount.  So borrowing an additional 100.00 would add 10.00 to the
   monthly payment.  Add an "payback_monthly_amount" method that
   reduces the balance by the monthly payment amount.
   Also add a method to get the value of the monthly payment,
   but do not allow clients to set it. *)


(* Problem 2(b)  *)
(* Add a method "to_loan" to every class that transforms an
   object to the corresponding value of type loan (where
   loan is the data type defined at the beginning of this file
   just before the statement of Problem 1(a)).  (It must return an
   element of type loan where the values of the arguments are
   determined from the values of the instance variables. *)


(*********************************************)
(* PROBLEM 3: Object-Oriented "Constructors" *)
(*********************************************)

(* Problem 3(a)  *)
(* Write a function that takes an argument, creates a note_payable
   object, and then uses the argument to update the balance.  Do the
   same for credit_card and bank_loan.  The function that
   creates a credit card must take an additional argument used to
   set the interest rate. *)

let construct_note_payable (f:float) =
  let note = new note_payable in
    (note#set_balance f; note)
;;

let construct_credit_card (f:float) (i:float) =
  let credit = new credit_card in
    (credit#set_balance f; credit#set_interest i; credit)
;;

let construct_bank_loan (f:float) (m:float) =
  let bank = new bank_loan in
    (bank#set_balance f; bank#set_monthly m; bank)
;;


(* Problem 3(b)  *)
(* Using the same data that you used to create your solution to 1(b),
   create one object of each class, and then create a list containing
   all of them.  You may have to use the coercion operator "<:" from
   Chapter 12 of "Real World OCaml".  (See the course notes.) *)

let a = construct_note_payable 100.;;
let b = construct_credit_card 50. 0.10;;
let c = construct_bank_loan 200. 0.0;;
let all : note_payable list = [a; (b :> note_payable); (c :> note_payable)];;


(* Problem 3(c)  *)
(* Redo Problem 1(a), writing the object-oriented version this time (a
   function that takes a list of objects of type note_payable and
   returns a list of balances.  Call your function on your list from
   Problem 3(b). *)

let rec get_oBalances1 (l:note_payable list) : fBalance list =
  match l with
    | [] -> []
    | hd::tl -> [hd#get_balance] @ get_oBalances1 tl
;;

let q3c = get_oBalances1 all;;


(****************************************************)
(* PROBLEM 4: Conversion to Function-Oriented Style *)
(****************************************************)

(* Write a function that returns a list of loan balances with the
   same return values as your solution to Problem 3(c), but this time,
   your function must first take a list of note_payable objects,
   convert them to elements of type loan, and then call your
   function from Problem 1(a) *) 

let get_oBalances2 (l:note_payable list) : fBalance list =
  let rec convert l =
    match l with
      | [] -> []
      | hd::tl -> [hd#to_loan] @ convert tl 
  in
  let loans = convert l in
    get_fBalances loans
;;

let q4 = get_oBalances2 all;;

