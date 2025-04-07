package come.codewiz.service;


import com.codewiz.model.Patient;
import com.codewiz.patient.*;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import come.codewiz.repository.PatientRepository;

@Service
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    @Override
    public void registerPatient(PatientRegistrationRequest request, StreamObserver<PatientRegistrationResponse> responseObserver) {
        Patient patient = new Patient(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress()
        );
        patient = patientRepository.save(patient);
        responseObserver.onNext(PatientRegistrationResponse.newBuilder().setPatientId(patient.id()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPatientDetails(PatientDetailsRequest request, StreamObserver<PatientDetails> responseObserver) {
        var patient = patientRepository.findById(request.getPatientId());
        if(patient.isPresent()) {
            var p = patient.get();
            responseObserver.onNext(PatientDetails.newBuilder()
                    .setPatientId(p.id())
                    .setFirstName(p.firstName())
                    .setLastName(p.lastName())
                    .setEmail(p.email())
                    .setPhone(p.phone())
                    .setAddress(p.address())
                    .build());
        }
        else {
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription("Patient not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }



}
