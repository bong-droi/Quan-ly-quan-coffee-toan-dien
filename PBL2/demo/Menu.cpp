#include "Menu.hpp"
#include <iostream>
#include <fstream>
#include <algorithm>
#include <string>
#include <sstream>
#include <ncurses.h>
#include <vector>
#include <map>
#include <iomanip>

using namespace std;

void Menu::taiMenu() {
    ifstream file("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/menu.txt");

    if (!file.is_open()) {
        cerr << "Khong the mo file menu.txt" << endl;
        return;
    }
    string line, ten;
    double gia;
    while (getline(file, line)) {
        stringstream ss(line);
        getline(ss, ten, ','); // tên và giá cách nhau bằng dấu phẩy
        ss >> gia;
        menu.emplace_back(ten, gia);
    }

    file.close();
}

void Menu::luuMenu() {
    ofstream file("/Users/miutranqt96gmail.com/Documents/Code-Document/pb2/PBL2/demo/menu.txt");

    if (!file.is_open()) {
        cerr << "Khong the mo file menu.txt" << endl;
        return;
    }
    for (const auto& mon : menu) {
        file << mon.getTen() << "," << mon.getGia() << endl; // Dùng dấu phẩy là ký tự ngăn cách
    }

    file.close();
}

void Menu::themMon(string ten, double gia) {
    menu.emplace_back(ten, gia);
    luuMenu();
    mvprintw(3, 10, "Them mon thanh cong!");
    refresh();
    getch();
}

void Menu::suaMon(int index, string tenMoi, double giaMoi) {
    if (index >= 0 && index < menu.size()) {
        menu[index].setTen(tenMoi);
        menu[index].setGia(giaMoi);
        luuMenu();
    }
}

void Menu::xoaMon(int index) {
    if (index >= 0 && index < menu.size()) {
        menu.erase(menu.begin() + index);
        luuMenu();
    }
}

void Menu::hienThiMenu() const {
    for (size_t i = 0; i < menu.size(); ++i) {
        cout << i + 1 << ". " << menu[i].getTen() << " - " << menu[i].getGia() << " VND" << endl;
    }
}

const vector<Mon>& Menu::layMenu() const {
    return menu;
}

enum class MenuOption { ThemMon, SuaMon, XoaMon, QuayLai }; // Định nghĩa enum cho các tùy chọn

// Hàm chọn món từ danh sách bằng ncurses
int Menu::chonTuDanhSach(const vector<string>& danhSach) {
    int selected = 0;
    int key;
    while (true) {
        clear();
        for (int i = 0; i < danhSach.size(); i++) {
            if (i == selected) {
                attron(A_REVERSE); // Đổi màu dòng đang chọn
                mvprintw(i + 1, 2, danhSach[i].c_str());
                attroff(A_REVERSE);
            } else {
                mvprintw(i + 1, 2, danhSach[i].c_str());
            }
        }
        refresh();
        key = getch();
        switch (key) {
            case KEY_UP:
                if (selected > 0) selected--;
                break;
            case KEY_DOWN:
                if (selected < danhSach.size() - 1) selected++;
                break;
            case 10: // Phím Enter
                return selected;
        }
    }
}

void Menu::quanLiMenu(Menu &menu) {
    initscr();            // Khởi tạo ncurses
    keypad(stdscr, TRUE);  // Kích hoạt phím mũi tên
    noecho();              // Tắt chế độ hiển thị đầu vào
    curs_set(0);           // Ẩn con trỏ

    vector<string> options = { "1. Them mon", "2. Sua mon", "3. Xoa mon", "4. Quay lai" };
    int selected = 0;
    int key;
    bool exitMenu = false;
    while (!exitMenu) {
        // Hiển thị các tùy chọn menu
        clear();
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                attron(A_REVERSE); // Đổi màu tùy chọn được chọn
                mvprintw(i + 1, 2, options[i].c_str());
                attroff(A_REVERSE);
            } else {
                mvprintw(i + 1, 2, options[i].c_str());
            }
        }
        refresh();

        // Xử lý phím người dùng nhập
        key = getch();
        switch (key) {
            case KEY_UP:
                if (selected > 0) selected--;
                break;
            case KEY_DOWN:
                if (selected < options.size() - 1) selected++;
                break;
            case 10: // Phím Enter
                switch (static_cast<MenuOption>(selected)) {
                    case MenuOption::ThemMon: {
                        echo();
                        curs_set(1);
                        char ten[50];
                        double gia;
                        
                        while (true) {
                            clear(); // Xóa màn hình để giao diện gọn gàng
                            mvprintw(options.size() + 2, 2, "Nhap ten mon: ");
                            getstr(ten);
                            bool tonTai = false;
                            for (const auto& mon : menu.layMenu()) { // Sử dụng layMenu() để lấy danh sách món
                                if (mon.getTen() == string(ten)) {
                                    mvprintw(options.size() + 3, 2, "Ten mon da ton tai. Vui long nhap lai.");
                                    refresh();
                                    getch(); // Đợi người dùng nhấn phím trước khi nhập lại
                                    tonTai = true;
                                    break;
                                }
                            }

                            if (!tonTai) {
                                break; // Tên món hợp lệ, thoát khỏi vòng lặp
                            }
                        }

                        mvprintw(options.size() + 3, 2, "Nhap gia mon: ");
                        scanw("%lf", &gia);
                        noecho();
                        curs_set(0);

                        // Thêm món vào menu và lưu vào file
                        menu.themMon(string(ten), gia);
                        break;
                    }


                    case MenuOption::SuaMon: {
                        vector<string> danhSach;
                        for (const auto& mon : menu.layMenu()) {
                            // Sử dụng ostringstream để định dạng chuỗi
                            ostringstream oss;
                            oss << left << setw(25) << mon.getTen() << setw(6) << mon.getGia() << "VND";
                            danhSach.push_back(oss.str());
                        }

                        int index = chonTuDanhSach(danhSach);
                        if (index < 0 || index >= danhSach.size()) {
                            mvprintw(danhSach.size() + 2, 2, "Lua chon khong hop le.");
                            refresh();
                            getch();
                            break;
                        }

                        echo();
                        curs_set(1);

                        char tenMoi[50];
                        double giaMoi;

                        mvprintw(danhSach.size() + 2, 2, "Nhap ten moi: ");
                        getstr(tenMoi); // Lấy tên mới
                        mvprintw(danhSach.size() + 3, 2, "Nhap gia moi: ");
                        scanw("%lf", &giaMoi); // Lấy giá mới

                        noecho();
                        curs_set(0);

                        menu.suaMon(index, string(tenMoi), giaMoi);

                        mvprintw(0, 20, "Sua mon thanh cong!");
                        refresh();
                        getch();

                        break;
                    }
                    case MenuOption::XoaMon: {
                        vector<string> danhSach;
                        for (const auto& mon : menu.layMenu()) {
                            // Sử dụng ostringstream để định dạng chuỗi
                            ostringstream oss;
                            oss << left << setw(25) << mon.getTen() << setw(6) << mon.getGia() << "VND";
                            danhSach.push_back(oss.str());
                        }

                        int index = chonTuDanhSach(danhSach);
                        if (index < 0 || index >= danhSach.size()) {
                            mvprintw(danhSach.size() + 2, 2, "Lua chon khong hop le.");
                            refresh();
                            getch();
                            break;
                        }

                        menu.xoaMon(index);
                        mvprintw(0, 20, "Xoa mon thanh cong!");
                        refresh();
                        getch();
                        break;
                    }
                    case MenuOption::QuayLai:
                        exitMenu = true;
                        break;
                }
                break;
        }
    }

    endwin(); 
}
