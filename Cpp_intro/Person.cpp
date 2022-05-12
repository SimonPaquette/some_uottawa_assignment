
#include "Person.h"
#include "OrganizationPayante.h"


Person::Person(std::string n, int a) {
    name = n;
    age = a;
    organizations = std::map<string, Organization*>();
};

Person::~Person()
{
    for (auto it = organizations.begin(); it != organizations.end(); it++)
        delete it->second;
}

void Person::addOrganization(Organization* organization)
{
  
    organizations.insert(pair<string, Organization*>(organization->getName(), organization));
  
}

void Person::removeOrganization(Organization* organization)
{

    auto it = organizations.find(organization->getName());
    if (it != organizations.end()) {
        organizations.erase(organization->getName());
    }
    else {
        throw std::invalid_argument("Person::removeOrganization");
    }
}


std::string Person::getName() const { return name; }
int Person::getAge() const { return age; }




string Person::getOrgNames()
{
    std::string name = "";
    for (auto it = organizations.begin(); it != organizations.end(); it++)
    {
        name = name + (*it).first + ", ";
    }
    return name;
}

float Person::getAPayer() {
    float total = 0;
    for (auto it = organizations.begin(); it != organizations.end(); it++) {

        OrganizationPayante* op = dynamic_cast<OrganizationPayante*>((*it).second);

        if (op != nullptr) {
            total += op->getFrais();
        }
    }
    return total;
}

string Person::printAPayer() {
    string name = "";

    for (auto it = organizations.begin(); it != organizations.end(); it++) {

        OrganizationPayante* op = dynamic_cast<OrganizationPayante*>((*it).second);

        if (op != nullptr) {
            name = name + op->getName() + ": " + to_string(op->getFrais()) + "; ";

        }
    }
    return name;

}

int Person::getSize() const {
    return organizations.size();
}



