#ifndef TAIKHOAN_H
#define TAIKHOAN_H
#include <string>
#include "ncurses.h"
using namespace std;

class TaiKhoan {
private:
    string id;
    string matKhau;
    bool isAdmin;
    
public:
    TaiKhoan();
    TaiKhoan(const string& id, const string& matKhau, bool isAdmin);
    string getId() const;
    void setId(const string& id);
    string getMatKhau() const;
    void setMatKhau(const string& matKhau);
    bool getIsAdmin() const;
    void setIsAdmin(bool isAdmin);
    void nhap(const vector<TaiKhoan>& danhSachTaiKhoan);
};

#endif // TAIKHOAN_H

