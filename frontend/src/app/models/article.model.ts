export class Article {
    url: string;

    realEstateType: string;

    title: string;

    city: string;

    price: number;

    nbRooms: number;
    landSurface: number;
    homeSurface: number;

    imageUrl: string

    imageUrls: string[];
    description: string;

    favorite: boolean;
    sold: boolean;

    internalProvider: string;
    externalProvider: string

    internalReference: string;
    externalReference: string;
}
