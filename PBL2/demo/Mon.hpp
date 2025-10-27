#ifndef MON_H
#define MON_H
#include <string>
using namespace std;

class Mon {
private:
    string ten;
    double gia;
    
public:
    Mon(const string& ten, double gia);
    string getTen() const;
    void setTen(const string& ten);
    double getGia() const;
    void setGia(double gia);
};


#endif // MON_H

