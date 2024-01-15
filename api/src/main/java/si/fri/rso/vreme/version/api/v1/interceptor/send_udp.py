import socket
import ssl

def send_message_over_tcp(message, host, port):
    context = ssl.create_default_context()

    with socket.create_connection((host, port)) as sock:
        with context.wrap_socket(sock, server_hostname=host) as ssock:
            ssock.sendall(message.encode())

send_message_over_tcp("Simple message over TCP", "70b30c4c-a54e-476a-a5ea-42d916818926-ls.logit.io", 21052)