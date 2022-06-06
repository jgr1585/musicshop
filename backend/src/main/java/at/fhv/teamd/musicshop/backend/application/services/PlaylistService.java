package at.fhv.teamd.musicshop.backend.application.services;

import at.fhv.teamd.musicshop.backend.domain.repositories.InvoiceRepository;
import at.fhv.teamd.musicshop.backend.infrastructure.RepositoryFactory;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class PlaylistService {
    private static final InvoiceRepository invoiceRepository = RepositoryFactory.getInvoiceRepositoryInstance();

    public List<AlbumDTO> getPlaylist(String username) throws CustomerNotFoundException {
        int customerNo = ServiceFactory
                .getCustomerServiceInstance()
                .getCustomerNoByUsername(username);

        return invoiceRepository.findInvoicesByCustomerNo(customerNo).stream()
                .flatMap(invoice -> invoice.getLineItems().stream())
                .map(li -> DTOProvider.buildArticleDTO(li.getMedium().getAlbum()))
                .collect(Collectors.toList());
    }
}
