package es.florida.examen;

import java.util.ArrayList;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

public class Examen {
	
	static String arrayTitol[] = { "El padrino", "El padrino II", "Doce hombres sin piedad", "La lista de Schindler", "Testigo de Cargo",
			"Luces de ciudad", "Cadena Perpetua" , "El gran dictador" };
	static double arrayNotes[] = {9.0,8.9,8.7,8.6,8.6,8.6,8.6,8.6};
	static int arrayAny[]= {1972,1974,1957,1993,1957,1931,1994,1940};
	static Scanner sc= new Scanner(System.in);
	

	public static void main(String[] args) {
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("Pelis");
		MongoCollection<Document> coleccion = database.getCollection("Top");
		MongoCursor<Document> cursor = coleccion.find().iterator();
		MongoCursor<Document> cursorFiltre;
		Bson query;
		
		Ejecuta uno por uno 
		
		Ejercicio1
		
		ArrayList<Document> listaDocs = new ArrayList<Document>();
		Document doc = new Document();
		for (int i = 0; i < arrayTitol.length; i++) {
			doc = new Document();
			doc.append("titol", arrayTitol[i]);
			doc.append("nota", arrayNotes[i]);
			doc.append("any", arrayAny[i]);
			listaDocs.add(doc);
		}
		coleccion.insertMany(listaDocs);
		
		Ejercicio2
		
		System.out.print("Elige una opcion de las siguientes: " + "\n1. Mostrar el titulo de una pelicula" + "\n2. Mostrar el titulo y una nota de una pelicula"
				+ "\nElige una opcion: "); int opcio = Integer.parseInt(sc.nextLine());
				
				if(opcio==1) {
					while(cursor.hasNext()) {
						JSONObject obj= new JSONObject(cursor.next().toJson());
						System.out.println(obj.getString("titol"));
					}
				}else {
					while(cursor.hasNext()) {
						JSONObject obj= new JSONObject(cursor.next().toJson());
						System.out.println(obj.getString("titol") + "-" + obj.getDouble("nota"));
					}
				}
		
		Ejercicio3	
		
		System.out.print("Introduce un año : "); int anyo= Integer.parseInt(sc.nextLine());
		query = lt("any",anyo);
		cursorFiltre = coleccion.find(query).iterator();
		while(cursorFiltre.hasNext()) {
			JSONObject obj2 = new JSONObject(cursorFiltre.next().toJson());
			System.out.println(obj2.getString("titol"));
		}
		
		Ejercicio4
		
		System.out.print("Introduce una pelicula a modificar: "); String titol= sc.nextLine();
		query = eq("titol",titol);
		System.out.print("Dime el nuevo año: "); int newAnyo= Integer.parseInt(sc.nextLine());
		System.out.print("Dime la nueva nota: "); double newNota= Double.parseDouble(sc.nextLine());
		coleccion.updateOne(query, new Document("$set", new Document("nota",newAnyo)));
		coleccion.updateOne(query, new Document("$set", new Document("any",newNota)));
		
		Ejercicio5
		
		ArrayList<Document> listaDocs = new ArrayList();
		System.out.print("Titol de la pelicula: "); String titol = sc.nextLine();
		System.out.print("Nota de la pelicula: "); double nota = Double.parseDouble(sc.nextLine());
		System.out.print("Any de la pelicula: " ); int anyN = Integer.parseInt(sc.nextLine());

		Document doc = new Document();
		doc.append("titol",titol);
		doc.append("nota", nota);
		doc.append("any", anyN);
		listaDocs.add(doc);
		coleccion.insertMany(listaDocs);
		
		Ejercicio6
		
		double notaMitja=0;
		double notaMax=0;
		double notaMin=10;
		while(cursor.hasNext()) {
			
			double notasConv=0;
			JSONObject obj3= new JSONObject(cursor.next().toJson());
			
			System.out.println(obj3.getDouble("nota"));
			double notas = obj3.getDouble("nota");
			notaMitja+=notas;
			notaMitja=notaMitja/coleccion.count();
			if(notaMax<notas) {
				notaMax=notas;

			}
			if(notaMin>notas) {
				notaMin=notas;
			}			
		}
		System.out.println("Nota media: " + Math.floor(notaMin) + "\nNota maxima: " + notaMax + "\nNota minima: " + notaMin);
		


	}}