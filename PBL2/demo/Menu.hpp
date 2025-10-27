#ifndef MENU_H
#define MENU_H
#include <vector>
#include <string>
#include "Mon.hpp"

class Menu {
private:
    vector<Mon> menu;
    
public:
    void taiMenu();
    void luuMenu();
    void themMon(string ten, double gia);
    void suaMon(int index, string tenMoi, double giaMoi);
    void xoaMon(int index);
    void hienThiMenu() const;
    void quanLiMenu(Menu &menu);
    int chonTuDanhSach(const vector<string>& danhsach);
    const vector<Mon>& layMenu() const;
};


#endif // MENU_H

