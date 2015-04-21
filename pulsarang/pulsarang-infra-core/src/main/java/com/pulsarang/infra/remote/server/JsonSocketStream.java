package com.pulsarang.infra.remote.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import com.pulsarang.core.util.CollectionJsonMapper;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.remote.RemoteServiceRequest;
import com.pulsarang.infra.remote.RemoteServiceResponse;

public class JsonSocketStream {
	private final Socket socket;
	private final DataInputStream dis;
	private final DataOutputStream dos;

	private final String version = InfraContextFactory.getInfraContext().getVersion();

	public JsonSocketStream(Socket socket) throws IOException {
		if (socket == null) {
			throw new IllegalArgumentException();
		}

		this.socket = socket;
		this.dis = new DataInputStream(socket.getInputStream());
		this.dos = new DataOutputStream(socket.getOutputStream());
	}

	public Socket getSocket() {
		return socket;
	}

	public void close() throws IOException {
		socket.close();
	}

	public void writeMap(Map<String, Object> map) throws IOException {
		String jsonRequest = CollectionJsonMapper.getInstance().toJson(map);
		if (jsonRequest == null) {
			jsonRequest = "";
		}

		byte[] bytes = jsonRequest.getBytes("utf-8");

		dos.writeUTF(version);
		dos.writeInt(bytes.length);
		dos.write(bytes);
		dos.flush();
	}

	public Map<String, Object> readMap() throws IOException {
		/* String version = */dis.readUTF();

		int size = dis.readInt();
		byte[] newBytes = new byte[size];
		int offset = 0;
		int nRemain = size;
		int nRead = 0;
		while ((nRead = dis.read(newBytes, offset, nRemain)) != -1) {
			offset += nRead;
			nRemain -= nRead;
			if (nRemain == 0) {
				break;
			}
		}

		String jsonResponse = new String(newBytes, "utf-8");
		if (jsonResponse.isEmpty()) {
			return null;
		}

		Map<String, Object> result = CollectionJsonMapper.getInstance().toMap(jsonResponse);

		return result;
	}

	public RemoteServiceResponse writeRequestAndGetResponse(RemoteServiceRequest request) throws IOException {
		writeMap(request);

		Map<String, Object> map = readMap();
		RemoteServiceResponse response = RemoteServiceResponse.fromMap(map, RemoteServiceResponse.class);

		return response;
	}
}
