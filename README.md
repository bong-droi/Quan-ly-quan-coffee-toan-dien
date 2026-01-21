# ☕ HỆ THỐNG QUẢN LÝ QUÁN COFFEE TOÀN DIỆN

## 1. Giới thiệu đề tài
Hệ thống Quản lý Quán Coffee Toàn Diện là một ứng dụng web được xây dựng nhằm hỗ trợ công tác quản lý và vận hành quán coffee một cách hiệu quả.  
Hệ thống giúp số hóa các nghiệp vụ như quản lý người dùng, quản lý menu, đặt hàng, quản lý ca làm việc, tính lương nhân viên, quản lý kho và thống kê – báo cáo doanh thu.

Dự án được phát triển theo mô hình **Client – Server**, áp dụng kiến trúc **MVC**, phù hợp cho việc mở rộng, bảo trì và triển khai thực tế.

---

## 2. Công nghệ sử dụng
- **Ngôn ngữ**: Python
- **Framework**: Django
- **Cơ sở dữ liệu**: SQLite (có thể mở rộng sang MySQL/PostgreSQL)
- **Frontend**: HTML, CSS, JavaScript
- **ORM**: Django ORM
- **Quản lý mã nguồn**: Git, GitHub

---

## 3. Các chức năng chính

### 🔐 Quản lý tài khoản
- Đăng ký / đăng nhập người dùng
- Phân quyền: Chủ quán, Nhân viên, Khách hàng
- Đổi mật khẩu, chỉnh sửa thông tin cá nhân

### ☕ Quản lý menu
- Thêm / sửa / xóa món
- Phân loại sản phẩm
- Quản lý hình ảnh và giá bán

### 🛒 Quản lý đơn hàng
- Tạo đơn tại quầy và online
- Xem chi tiết hóa đơn
- Hủy đơn, lưu lý do hủy
- In / xuất hóa đơn

### 📦 Quản lý kho
- Nhập nguyên liệu
- Xuất – hao hụt – hư hỏng
- Theo dõi số lượng tồn kho

### 👥 Quản lý nhân viên & ca làm
- Đăng ký ca làm
- Phân công ca
- Theo dõi lịch làm việc cá nhân

### 💰 Quản lý lương
- Cấu hình lương cơ bản / lương theo giờ
- Tính lương theo ca làm
- Xem bảng lương nhân viên

### 📊 Báo cáo – thống kê
- Thống kê doanh thu
- Báo cáo theo ngày / tháng
- Dashboard tổng quan

---

## 4. Kiến trúc hệ thống
Hệ thống được xây dựng theo kiến trúc **MVT (Model – View – Template)** của Django:

- **Model**: Định nghĩa cấu trúc dữ liệu, ánh xạ cơ sở dữ liệu thông qua Django ORM và xử lý nghiệp vụ.
- **View**: Tiếp nhận request từ người dùng, xử lý logic nghiệp vụ và trả về response tương ứng.
- **Template**: Xây dựng giao diện người dùng bằng HTML, CSS và JavaScript.

Kiến trúc MVT giúp hệ thống có cấu trúc rõ ràng, dễ bảo trì, dễ mở rộng và phù hợp với các ứng dụng web quy mô vừa và lớn.

---

## 5. Hướng dẫn cài đặt & chạy hệ thống

### 🔹 Bước 1: Clone project
```bash
git clone https://github.com/bong-droi/Quan-ly-quan-coffee-toan-dien.git
cd Quan-ly-quan-coffee-toan-dien
