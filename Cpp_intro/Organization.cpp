
#include "Person.h"


Organization::Organization(std::string n) {
    name = n;
    members = std::multimap<string, Person*>();
};


Organization::~Organization()
{
    for (auto it = members.begin(); it != members.end(); it++)
        delete it->second;
}

void Organization::addMember(Person* person)
{
    members.insert(pair<string, Person*>(person->getName(), person));

}

void Organization::removePerson(Person* person)
{
    auto it = members.find(person->getName());
    if (it != members.end()) {
        members.erase(person->getName());
    }
    else {
        throw std::invalid_argument("Organization::removePerson");
    }
    

}

string Organization::getMemberNames()
{
    string name = "";
    for (auto it = members.begin(); it != members.end(); it++) {
        name = name + (*it).first + ":" + std::to_string((*it).second->getAge()) + "; ";
    }
    return name;
}



string Organization::getName() const { return name; }

int Organization::getSize() const {
    return members.size();
}



