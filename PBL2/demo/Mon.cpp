#include "Mon.hpp"
#include <iostream>
using namespace std;

Mon::Mon(const string& ten, double gia) : ten(ten), gia(gia) {}

string Mon::getTen() const {return ten;}
void Mon::setTen(const string& ten) {this->ten = ten;}

double Mon::getGia() const {return gia;}
void Mon::setGia(double gia) {this->gia = gia;}
