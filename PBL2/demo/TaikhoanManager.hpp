#ifndef TAIKHOANMANAGER_H
#define TAIKHOANMANAGER_H
#include <vector>
#include "TaiKhoan.hpp"
using namespace std;

class TaiKhoanManager {
private:
    vector<TaiKhoan> taiKhoans;
    
public:
    void taiKhoansList();
    void luuTaiKhoans();
    void taoTaiKhoan();
    void xemTaiKhoans() const;
    bool dangNhap(TaiKhoan& taiKhoanUser) const;
};

#endif // TAIKHOANMANAGER_H
