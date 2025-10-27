#ifndef KHUNGHIENTHI_H
#define KHUNGHIENTHI_H

#include <ncurses.h>
#include <vector>
#include <string>
using namespace std;

class KhungHienThi {
private:
    vector<string> menuItems;
    int selectedItem;

public:
    KhungHienThi(const vector<string>& items);
    void hienThiMenu();
    int chonItem();
};

#endif

