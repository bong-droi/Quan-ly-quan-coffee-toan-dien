#include <iostream>
#include "TaiKhoan.hpp"
using namespace std;

TaiKhoan::TaiKhoan() : id(""), matKhau(""), isAdmin(false) {}

TaiKhoan::TaiKhoan(const string& id, const string& matKhau, bool isAdmin): id(id), matKhau(matKhau), isAdmin(isAdmin) {}


string TaiKhoan::getId() const {return id;}
void TaiKhoan::setId(const string& id) {this->id = id;}

string TaiKhoan::getMatKhau() const {return matKhau;}
void TaiKhoan::setMatKhau(const string& matKhau) {this->matKhau = matKhau;}

bool TaiKhoan::getIsAdmin() const {return isAdmin;}
void TaiKhoan::setIsAdmin(bool isAdmin) {this->isAdmin = isAdmin;}


void TaiKhoan::nhap(const vector<TaiKhoan>& danhSachTaiKhoan) {
    clear();
    int row = 0;

    while (true) {
        mvprintw(row++, 0, "Nhap ID moi: ");
        refresh();
        char tempId[50];
        echo();
        getstr(tempId);
        noecho();

        if (strlen(tempId) == 0) {
            mvprintw(row++, 0, "ID khong duoc de trong. Nhap lai!");
            refresh();
            getch();
            row = 0;
            clear();
            continue;
        }

        // Kiểm tra ID đã tồn tại trong danh sách
        bool idTonTai = false;
        for (const auto& tk : danhSachTaiKhoan) {
            if (tk.getId() == tempId) {
                idTonTai = true;
                break;
            }
        }

        if (idTonTai) {
            mvprintw(row++, 0, "ID da ton tai. Vui long nhap lai!");
            refresh();
            getch();
            row = 0;
            clear();
        } else {
            id = tempId; // Lưu ID nếu không trùng
            break;
        }
    }

    mvprintw(row++, 0, "Nhap mat khau moi: ");
    refresh();
    char tempPass[50];
    echo();
    getstr(tempPass);
    noecho();

    if (strlen(tempPass) == 0) {
        mvprintw(row++, 0, "Mat khau khong duoc de trong. Nhap lai!");
        refresh();
        getch();
        clear();
        nhap(danhSachTaiKhoan); 
        return;
    }
    matKhau = tempPass;

    // Xác định tài khoản có phải admin hay không
    mvprintw(row++, 0, "Tai khoan la admin? (y/n): ");
    refresh();
    char laAdmin;
    do {
        laAdmin = getch();
    } while (laAdmin != 'y' && laAdmin != 'n');
    isAdmin = (laAdmin == 'y');

    // Thông báo tạo tài khoản thành công
    mvprintw(row++, 0, "Tao tai khoan thanh cong!");
    refresh();
    getch(); // Đợi người dùng nhấn phím trước khi tiếp tục
    clear();
}
