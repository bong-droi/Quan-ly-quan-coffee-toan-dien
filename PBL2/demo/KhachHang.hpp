#ifndef KHACHHANG_H
#define KHACHHANG_H
#include "TaiKhoan.hpp"
#include "Menu.hpp"
#include <map>
#include <string>

class KhachHang : public TaiKhoan {
public:
    KhachHang(const string& id, const string& pass, bool isAdmin);
    void goiMon(const Menu& menu, map<string, pair<double, int>>& order);
};

#endif // KHACHHANG_H
