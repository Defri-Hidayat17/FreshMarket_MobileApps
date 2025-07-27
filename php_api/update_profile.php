<?php
header("Content-Type: application/json");

// Panggil koneksi
include "koneksi.php";

// Ambil data dari POST
$name     = $_POST['name'] ?? '';
$email    = $_POST['email'] ?? '';
$bio      = $_POST['bio'] ?? '';
$birthday = $_POST['birthday'] ?? '';
$gender   = $_POST['gender'] ?? '';
$photo    = $_POST['photo'] ?? '';

// Cek apakah user sudah ada berdasarkan email
$cek = $koneksi->prepare("SELECT email FROM users WHERE email = ?");
$cek->bind_param("s", $email);
$cek->execute();
$hasil = $cek->get_result();

if ($hasil->num_rows > 0) {
    // Update data
    $stmt = $koneksi->prepare("UPDATE users SET name=?, bio=?, birthday=?, gender=?, photo=? WHERE email=?");
    $stmt->bind_param("ssssss", $name, $bio, $birthday, $gender, $photo, $email);
    $stmt->execute();
    $stmt->close();
    echo json_encode(["status" => "success", "message" => "Data berhasil diperbarui"]);
} else {
    // Insert data baru
    $stmt = $koneksi->prepare("INSERT INTO users (name, email, bio, birthday, gender, photo) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssss", $name, $email, $bio, $birthday, $gender, $photo);
    $stmt->execute();
    $stmt->close();
    echo json_encode(["status" => "success", "message" => "Data baru berhasil ditambahkan"]);
}
$cek->close();
$koneksi->close();
?>
