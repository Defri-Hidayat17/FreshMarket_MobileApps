<?php
header("Content-Type: application/json");
include "koneksi.php"; // sekarang koneksi ada di $koneksi

$email = $_GET['email'] ?? '';

if (empty($email)) {
    echo json_encode(["status" => "error", "message" => "Email tidak ditemukan"]);
    exit;
}

$stmt = $koneksi->prepare("SELECT * FROM users WHERE email=?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $data = $result->fetch_assoc();
    echo json_encode(["status" => "success", "data" => $data]);
} else {
    echo json_encode(["status" => "error", "message" => "Data tidak ditemukan"]);
}

$stmt->close();
$koneksi->close();
?>
