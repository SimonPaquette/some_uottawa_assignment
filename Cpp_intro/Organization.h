#ifndef OrganizationH
#define OrganizationH 

#include <map>

#include <iostream>
#include <string>
#include <stdlib.h>
using namespace std;
class Person;

class Organization {
    string name;
    std::multimap<string, Person*> members;

public:

    Organization(std::string n = "default");
    virtual ~Organization();
    void addMember(Person* members);
    void removePerson(Person* members);
    string getMemberNames();
    string getName() const;
    int getSize() const;


};

#endif 