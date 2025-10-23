# Quá Trình Xây Dựng RESTful API Với Spring Boot Và MySQL

## Giới Thiệu
Dự án này xây dựng một RESTful API đơn giản để quản lý thông tin của các "actor" (diễn viên) trong một cơ sở dữ liệu MySQL. Các tính năng chính bao gồm:
- Xem danh sách tất cả các actor.
- Xem chi tiết thông tin của một actor.
- Thêm mới một actor.
- Xóa một actor.
- Cập nhật thông tin của một actor.

Dự án sử dụng Spring Boot (một framework Java giúp phát triển ứng dụng web nhanh chóng) và kết nối với MySQL. Chúng ta sẽ không sử dụng DTO (Data Transfer Object) hay Mapper, mà trực tiếp dùng Entity để đơn giản hóa.

## Công Cụ Và Dependencies
- **Spring Boot**: Phiên bản 3.6 (khung chính để xây dựng API).
- **Java**: Phiên bản 21 (ngôn ngữ lập trình).
- **Maven**: Công cụ quản lý dự án và thư viện.
- **Dependencies**:
  - **Spring Web**: Để tạo API RESTful (xử lý request như GET, POST).
  - **Spring Data JPA**: Để kết nối và thao tác với database MySQL qua JPA (Java Persistence API).
  - **MySQL Driver**: Để kết nối với MySQL.
  - **Spring Boot DevTools**: Hỗ trợ phát triển nhanh (tự động reload khi thay đổi code).
  - **Lombok**: Giảm code thừa bằng cách tự động tạo getter/setter.

## Bước 1: Khởi Tạo Dự Án
1. Truy cập website https://start.spring.io.
2. Cấu hình như sau:
   - **Project**: Chọn Maven.
   - **Language**: Chọn Java.
   - **Spring Boot**: Chọn phiên bản 3.6.
   - **Group**: Nhập `com.web`.
   - **Artifact**: Nhập `rest-api-demo`.
   - **Name**: Nhập `rest-api-demo`.
   - **Description**: Nhập `Demo project for Spring Boot`.
   - **Package name**: Nhập `com.web.rest-api-demo`.
   - **Packaging**: Chọn Jar.
   - **Java**: Chọn 21.
3. Thêm các Dependencies: Spring Web, Spring Data JPA, MySQL Driver, Spring Boot DevTools, Lombok.
4. Nhấn nút **Generate** để tải file ZIP về.
5. Giải nén file ZIP và mở dự án trong IDE (như IntelliJ IDEA hoặc Eclipse).

## Bước 2: Cấu Hình Database
1. Mở file `src/main/resources/application.properties` trong dự án.
2. Thêm các dòng sau để kết nối với MySQL (thay đổi username và password theo thông tin của bạn):
