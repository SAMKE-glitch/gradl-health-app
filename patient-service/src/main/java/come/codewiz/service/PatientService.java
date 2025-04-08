package come.codewiz.service;

import com.codewiz.model.Patient;
import com.codewiz.patient.*;
import come.codewiz.repository.PatientRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void registerPatient(PatientRegistrationRequest request, StreamObserver<PatientRegistrationResponse> responseObserver) {
        // Create a new Patient object using data from the request
        Patient patient = new Patient(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress()
        );

        // Save the patient in the repository
        patient = patientRepository.save(patient);

        // Create the response message including the patient ID and a success message
        PatientRegistrationResponse response = PatientRegistrationResponse.newBuilder()
                .setPatientId(patient.id()) // Set patient ID
                .setMessage("Patient created successfully") // Set success message
                .build();

        // Send the response back to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPatientDetails(PatientDetailsRequest request, StreamObserver<PatientDetails> responseObserver) {
        var patient = patientRepository.findById(request.getPatientId());
        if (patient.isPresent()) {
            var p = patient.get();
            responseObserver.onNext(PatientDetails.newBuilder()
                    .setPatientId(p.id())
                    .setFirstName(p.firstName())
                    .setLastName(p.lastName())
                    .setEmail(p.email())
                    .setPhone(p.phone())
                    .setAddress(p.address())
                    .build());
        } else {
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("Patient not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }
}
