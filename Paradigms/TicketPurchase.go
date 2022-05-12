// Simon Paquette 300044038
// Devoir 1 - CSI2520
// Partie 1
package main

import (
	"fmt"
	"time"
	"strconv"
)

func main() {

	prime := newDefaultCategory(PRIME)
	prime.base = 35
	standart := newDefaultCategory(STANDART)
	standart.base = 25
	special := newDefaultCategory(SPECIAL)
	special.base = 15

	comedie := newDefaultComedy()
	comedie.play.showStart = time.Date(2020, 03, 3, 19, 30, 0, 0, time.UTC)
	comedie.play.showEnd = time.Date(2020, 03, 3, 22, 0, 0, 0, time.UTC) 
	
	tragedie := newDefaultTragedy()
	tragedie.play.showStart = time.Date(2020, 04, 10, 20, 0, 0, 0, time.UTC)
	tragedie.play.showEnd = time.Date(2020, 04, 10, 23, 0, 0, 0, time.UTC) 
	
	spectacles := []Show {comedie, tragedie}

	theatre := NewTheatre(25, 5, spectacles)

	for i:=0; i < len(theatre.seatList); i++ {
		row := theatre.seatList[i].row

		if (row == 1) {
			theatre.seatList[i].cat = prime
		} else if (row == 5) {
			theatre.seatList[i].cat = special
		} else {
			theatre.seatList[i].cat = standart
		}

	}

	


	for {
		fmt.Printf("\n\nAchat en cours...\n")
		var nom string
		fmt.Printf("Quel est votre nom? ")
		fmt.Scanln(&nom)
		var s Show

		for {
			var pieceInput string
			var piece string = ""
			

			fmt.Printf("Quel est votre piece? Tartuffe ou Macbeth : ")
			fmt.Scanln(&pieceInput)

			for _, show := range theatre.showList {
				if (show.getName() == pieceInput) {
					piece = pieceInput
					s = show
				}
			}

			if piece == "" {
				fmt.Printf("\nCette piece nexiste pas\n")
			} else {
				break
			}
		}

		for {
			var catInput string
			var numberInput string

			fmt.Printf("Quel categorie? ")
			fmt.Scanln(&catInput)

			fmt.Printf("Quel numero de siege (1 a NumberOfSeat) ? ")
			fmt.Scanln(&numberInput)

			found := false

			for index, _ := range theatre.seatList {
				seat := theatre.seatList[index]
				cat := seat.cat.name
				n := strconv.Itoa(int(seat.number))


				if (catInput == cat && numberInput == n) {
					found = true
					
					t := NewTicket(nom, &seat, &s) 
					
					isAdd := s.addPurchase(t)
					if (isAdd) {
						fmt.Printf("\nAchat du billet...\n ")
					} else {
						fmt.Printf("\nbillet deja achete.\n ")

					}
				}	
			} 

			if (!found)  {
				fmt.Printf("\nCe siege nexiste pas ")
			} else {
				break
			}


			
			
		}

		

	}


}

const (
	PRIME = "Prime"
	STANDART = "Standard"
	SPECIAL = "Special"
)

type Play struct {
	name string
	purchased []Ticket
	showStart time.Time
	showEnd time.Time
}

type Comedy struct {
	laughs float32
	deaths int32
	play Play
}

type Tragedy struct {
	laughs float32
	deaths int32
	play Play
}

type Show interface {
	getName() string
	getShowStart() time.Time
	getShowEnd() time.Time
	addPurchase(*Ticket) bool
	isNotPurchased(*Ticket) bool
}

type Seat struct {
	number int32
	row int32
	cat *Category
}

type Category struct {
	name string
	base float32
}

type Ticket struct {
	customer string
	siege *Seat
	s *Show
}

type Theatre struct {
	seatList []Seat
	showList []Show
}

func NewSeat(number int32, row int32, cat *Category) (seat *Seat) {
	seat = new(Seat)
	seat.number = number
	seat.row = row
	seat.cat = cat
	return
}

func NewTicket(customer string, siege *Seat, s *Show) (ticket *Ticket) {
	ticket = new(Ticket)
	ticket.customer = customer
	ticket.siege = siege
	ticket.s = s
	return
}

func NewTheatre(numberOfSeat int32, numberOfRow int32, showList []Show) (theatre *Theatre) {
	theatre = new(Theatre)
	theatre.showList = showList
	theatre.seatList = make([]Seat, 0, numberOfSeat*2) 
	
	var i int32 = 0
	for i < numberOfSeat {
		s := new(Seat)
		s.number = i + 1
		s.row = (i/(numberOfSeat/numberOfRow)) + 1
		theatre.seatList = append(theatre.seatList, *s)
		i++
	}
	return
}

func newDefaultComedy() (comedy *Comedy) {
	comedy = new(Comedy)
	comedy.laughs = 0.2
	comedy.deaths = 0
	comedy.play.name = "Tartuffe"
	comedy.play.purchased = make([]Ticket, 0, 100)
	comedy.play.showStart = time.Date(2020, 03, 3, 16, 0, 0, 0, time.UTC)
	comedy.play.showEnd = time.Date(2020, 03, 3, 17, 20, 0, 0, time.UTC)
	return
}

func newDefaultTragedy() (tragedy *Tragedy) {
	tragedy = new(Tragedy)
	tragedy.laughs = 0.0
	tragedy.deaths = 12
	tragedy.play.name = "Macbeth"
	tragedy.play.purchased = make([]Ticket, 0, 100)
	tragedy.play.showStart = time.Date(2020, 04, 16, 9, 30, 0, 0, time.UTC)
	tragedy.play.showEnd = time.Date(2020, 04, 16, 12, 30, 0, 0, time.UTC)
	return
}

func newDefaultCategory(name string) (c *Category) {
	c = new(Category)
	c.name = name
	c.base = 25.0
	return
}

func (s *Comedy) getName() string {
	return s.play.name
}

func (s *Comedy) getShowStart() time.Time {
	return s.play.showStart
}
 
func (s *Comedy) getShowEnd() time.Time {
	return s.play.showEnd
}

func (s *Comedy) addPurchase(t *Ticket) bool {
	if s.isNotPurchased(t) {
		s.play.purchased = append(s.play.purchased, *t)
		return true
	}
	return false	
}

func (s *Comedy) isNotPurchased(t *Ticket) bool {
	x := t.siege.number
	for index, _ := range s.play.purchased {
		y := s.play.purchased[index].siege.number
		if y == x {
			return false
		}
	}
	return true
}

func (s *Tragedy) getName() string {
	return s.play.name
}

func (s *Tragedy) getShowStart() time.Time {
	return s.play.showStart
}
 
func (s *Tragedy) getShowEnd() time.Time {
	return s.play.showEnd
}

func (s *Tragedy) addPurchase(t *Ticket) bool {
	if s.isNotPurchased(t) {
		s.play.purchased = append(s.play.purchased, *t)
		return true
	}
	return false	
}

func (s *Tragedy) isNotPurchased(t *Ticket) bool {
	x := t.siege.number
	for _, ticket := range s.play.purchased {
		y := ticket.siege.number
		if y == x {
			return false
		}
	}
	return true
}


