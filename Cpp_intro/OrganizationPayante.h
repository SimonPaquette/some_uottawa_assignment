
#ifndef ORGANISATIONPAYANTE_H 
#define ORGANISATIONPAYANTE_H 

#include "Organization.h"

class OrganizationPayante : public Organization {
private:
    float fraisAnnuel;

public:
    OrganizationPayante(std::string name) : Organization(name) {}



    OrganizationPayante(const OrganizationPayante& t) {
        fraisAnnuel = t.fraisAnnuel;
    }

    int getFrais() {
        return fraisAnnuel;
    }

    void setFrais(int frais) {
        fraisAnnuel = frais;
    }



};

#endif

