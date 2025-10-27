#ifndef HOADON_H
#define HOADON_H
#include <map>
#include <string>
#include "ncurses.h"
using namespace std;

class HoaDon {
public:
    void inHoaDon(const map<string, pair<double, int>>& order);
    void xemHoaDon() const;
    void xemDoanhThu() const;
};

#endif // HOADON_H


