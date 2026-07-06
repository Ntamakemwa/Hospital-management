package spring.hospitalmanagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.hospitalmanagement.DTO.ServiceRequestDTO;
import spring.hospitalmanagement.DTO.ServiceResponseDTO;
import spring.hospitalmanagement.Exception.DuplicateResourceException;
import spring.hospitalmanagement.Exception.ResourceNotFoundException;
import spring.hospitalmanagement.Mapper.ServiceMapper;
import spring.hospitalmanagement.Model.Hospital;
import spring.hospitalmanagement.Model.HospitalService;
import spring.hospitalmanagement.Repository.HospitalRepository;
import spring.hospitalmanagement.Repository.HospitalServiceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalService {

    private final HospitalServiceRepository serviceRepository;
    private final HospitalRepository hospitalRepository;
    private final ServiceMapper serviceMapper;

    @Transactional
    public ServiceResponseDTO createService(ServiceRequestDTO request, Long hospitalId) {

        if (serviceRepository.existsByNameAndHospitalId(request.getName(), hospitalId)) {
            throw new DuplicateResourceException("Service already exists in this hospital");
        }

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found"));

        HospitalService service = new HospitalService();
        service.setName(request.getName());
        service.setHospital(hospital);
        serviceRepository.save(service);

        return serviceMapper.toResponse(service);
    }

    @Transactional
    public ServiceResponseDTO updateService(Long serviceId, ServiceRequestDTO request) {
        HospitalService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setName(request.getName());
        serviceRepository.save(service);

        return serviceMapper.toResponse(service);
    }

    @Transactional
    public void deleteService(Long serviceId) {
        HospitalService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        serviceRepository.delete(service);
    }

    public List<ServiceResponseDTO> getServicesByHospital(Long hospitalId) {
        return serviceRepository.findByHospitalId(hospitalId)
                .stream()
                .map(serviceMapper::toResponse)
                .toList();
    }
}