#include "Khachhang.hpp"
#include "HoaDon.hpp"
#include <iostream>
#include <ncurses.h>
#include <vector>
#include <iomanip>

using namespace std;

KhachHang::KhachHang(const string& id, const string& pass, bool isAdmin)
    : TaiKhoan(id, pass, isAdmin) {}

void KhachHang::goiMon(const Menu& menu, map<string, pair<double, int>>& order) {
    initscr();
    keypad(stdscr, TRUE);
    noecho();
    curs_set(0);

    // Tạo danh sách menu động
    const auto& menuList = menu.layMenu();
    int ch;
    int selectedIndex = 0; // Chỉ số của mục được chọn

    while (true) {
        clear();

        // Hiển thị mục "Tính tiền" và "Đăng xuất"
        if (selectedIndex == 0) {
            attron(A_REVERSE);
            mvprintw(0, 2, "Tinh tien");
            attroff(A_REVERSE);
        } else {
            mvprintw(0, 2, "Tinh tien");
        }

        if (selectedIndex == 1) {
            attron(A_REVERSE);
            mvprintw(1, 2, "Dang xuat");
            attroff(A_REVERSE);
        } else {
            mvprintw(1, 2, "Dang xuat");
        }

        // Hiển thị menu các món ăn
        for (int i = 0; i < menuList.size(); ++i) {
            if (selectedIndex == i + 2) { // Mục món ăn đang được chọn
                attron(A_REVERSE);
                mvprintw(i + 2, 2, "%-20s %10.f VND", menuList[i].getTen().c_str(), menuList[i].getGia());
                attroff(A_REVERSE);
            } else { // Các mục còn lại
                mvprintw(i + 2, 2, "%-20s %10.f VND", menuList[i].getTen().c_str(), menuList[i].getGia());
            }
        }

        // Nhận phím điều hướng
        ch = getch();

        if (ch == KEY_UP) {
            selectedIndex = (selectedIndex - 1 + menuList.size() + 2) % (menuList.size() + 2); // Di chuyển lên
        } else if (ch == KEY_DOWN) {
            selectedIndex = (selectedIndex + 1) % (menuList.size() + 2); // Di chuyển xuống
        } else if (ch == '\n') { // Xử lý khi nhấn Enter
            if (selectedIndex == 0) { // "Tính tiền"
                break; // Kết thúc và in hóa đơn
            } else if (selectedIndex == 1) { // "Đăng xuất"
                // Hiển thị menu phụ đăng xuất
                vector<string> logoutMenu = {"1. Dang nhap lai", "2. Thoat"};
                int logoutIndex = 0;

                while (true) {
                    clear();
                    for (int i = 0; i < logoutMenu.size(); ++i) {
                        if (i == logoutIndex) {
                            attron(A_REVERSE);
                            mvprintw(i + 1, 2, logoutMenu[i].c_str());
                            attroff(A_REVERSE);
                        } else {
                            mvprintw(i + 1, 2, logoutMenu[i].c_str());
                        }
                    }

                    ch = getch();

                    if (ch == KEY_UP) {
                        logoutIndex = (logoutIndex - 1 + logoutMenu.size()) % logoutMenu.size();
                    } else if (ch == KEY_DOWN) {
                        logoutIndex = (logoutIndex + 1) % logoutMenu.size();
                    } else if (ch == '\n') {
                        if (logoutIndex == 0) {
                            endwin();
                            return; // Quay lại đăng nhập
                        } else if (logoutIndex == 1) {
                            endwin();
                            exit(0); // Thoát chương trình
                        }
                    }
                }
            } else { // Chọn món ăn
                int menuIndex = selectedIndex - 2; // Vì 2 mục đầu là "Tính tiền" và "Đăng xuất"
                const Mon& mon = menuList[menuIndex];

                int soLuong;
                echo();
                curs_set(1);
                mvprintw(menuList.size() + 4, 2, "Nhap so luong: ");
                scanw("%d", &soLuong);
                noecho();
                curs_set(0);

                if (order.find(mon.getTen()) != order.end()) {
                    order[mon.getTen()].second += soLuong;
                } else {
                    order[mon.getTen()] = make_pair(mon.getGia(), soLuong);
                }
                mvprintw(menuList.size() + 5, 2, "Da them mon '%s' vao danh sach!", mon.getTen().c_str());
                getch();
            }
        }
    }

    // In hóa đơn sau khi chọn "Tính tiền"
    endwin();
    HoaDon hoaDon;
    hoaDon.inHoaDon(order);
    getch();
}
