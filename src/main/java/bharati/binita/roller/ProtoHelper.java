package bharati.binita.roller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.InvalidProtocolBufferException;

import bharati.binita.roller.protobuf.PersonProto;

/**
 * 
 * @author binita.bharati@gmail.com
 * Protobuf utility that interfaces with protobuf APIs
 *
 */

public class ProtoHelper {
	
	
	public static byte[] serializeToProto(Person person) {
		PersonProto.Person protoPersonInstance = PersonProto.Person.newBuilder().setId(person.getId()).
				setName(person.getName()).build();
		return protoPersonInstance.toByteArray();
		
	}
	
	public static Person deserializeFromProto(byte[] byteAry) throws Exception {
		PersonProto.Person protoPerson = PersonProto.Person.parseFrom(byteAry);
		return new Person(protoPerson.getId(), protoPerson.getName());
	}
	
	public static void writeDelimitedToFile(FileOutputStream fos, Person person) {
		PersonProto.Person protoPersonInstance = PersonProto.Person.newBuilder().setId(person.getId()).
				setName(person.getName()).build();
		try {
			protoPersonInstance.writeDelimitedTo(fos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static int readDelimitedEntriesFromFile(InputStream fos) {
		int personCount = 0;
		while (true) {
			try {
				PersonProto.Person protoPerson = PersonProto.Person.parseDelimitedFrom(fos);
				if (protoPerson == null) {
					return personCount;
				}
				personCount++;
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
