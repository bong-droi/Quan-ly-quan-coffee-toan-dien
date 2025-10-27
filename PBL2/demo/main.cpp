#include <iostream>
#include "Menu.hpp"
#include "TaiKhoanManager.hpp"
#include "HoaDon.hpp"
#include "KhungHienThi.hpp"
#include "KhachHang.hpp"
#include <map>
using namespace std;

int main() {
    initscr();
    keypad(stdscr, TRUE);
    noecho();
    curs_set(0);

    Menu menu;
    menu.taiMenu();

    TaiKhoanManager tkManager;
    tkManager.taiKhoansList();

    HoaDon hoaDon;
    bool tieptuc = true;

    while (tieptuc) {
        TaiKhoan taiKhoanHienTai("", "", false);

        noecho();
        curs_set(1);
        clear();
        if (tkManager.dangNhap(taiKhoanHienTai)) {
            curs_set(0);
            noecho();
            bool dangNhap = true;

            while (dangNhap) {
                if (taiKhoanHienTai.getIsAdmin()) {
                    vector<string> adminItems = {
                        "1. Quan ly menu",
                        "2. Xem Doanh Thu",
                        "3. Xem Hoa don",
                        "4. Quan ly tai khoan",
                        "5. Dang xuat"
                    };
                    KhungHienThi adminMenu(adminItems);
                    adminMenu.hienThiMenu();
                    int chon = adminMenu.chonItem();

                    switch (chon) {
                        case 0:
                            menu.quanLiMenu(menu);
                            break;
                        case 1:
                            hoaDon.xemDoanhThu();
                            break;
                        case 2:
                            hoaDon.xemHoaDon();
                            break;

                        case 3: { 
                            vector<string> tkItems = {
                                "1. Tao tai khoan",
                                "2. Xem danh sach tai khoan",
                                "3. Quay lai"
                            };
                                
                                KhungHienThi tkMenu(tkItems);
                                tkMenu.hienThiMenu();
                                int chonTK = tkMenu.chonItem();
                                
                                switch (chonTK) {
                                    case 0:
                                        tkManager.taoTaiKhoan();
                                        break;
                                    case 1:
                                        tkManager.xemTaiKhoans();
                                        getch();
                                        break;
                                    case 2:
                                        break;
                                }
                                break;
                        }
                        case 4: {
                            vector<string> logoutOptions = {"1. Dang nhap lai", "2. Thoat"};
                            KhungHienThi logoutMenu(logoutOptions);
                            logoutMenu.hienThiMenu();
                            int chonLogout = logoutMenu.chonItem();

                            if (chonLogout == 0) {
                                dangNhap = false;
                            } else {
                                tieptuc = false;
                                dangNhap = false;
                            }
                            break;
                        }
                    }
                } else {
                    //vector<string> Options = {"1. Dat hang", "2. Dang xuat"};
                    map<string, pair<double, int>> order;
                    KhachHang khachHang(taiKhoanHienTai.getId(), taiKhoanHienTai.getMatKhau(), false);
                    khachHang.goiMon(menu, order);
                    
                    vector<string> logoutOptions = {"1. Dang nhap lai", "2. Thoat"};
                    KhungHienThi logout(logoutOptions);
                    logout.hienThiMenu();
                    int chonLogout = logout.chonItem();

                    if (chonLogout == 0) {
                        dangNhap = false;
                    } else {
                        tieptuc = false;
                        dangNhap = false;
                    }
                }
            }
        }
    }

    endwin();
    return 0;
}
