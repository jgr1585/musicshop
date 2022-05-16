/*
 * Musicshop API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 3.0.1
 * Contact: techsupport@musicshop.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 *
 * Swagger Codegen version: 3.0.34
 *
 * Do not edit the class manually.
 *
 */
import {ApiClient} from '../ApiClient';
import {ArticleDTO} from './ArticleDTO';
import {ArtistDTO} from './ArtistDTO';
import {MediumDTO} from './MediumDTO';
import {SongDTO} from './SongDTO';

/**
 * The AlbumDTO model module.
 * @module model/AlbumDTO
 * @version 3.0.1
 */
export class AlbumDTO extends ArticleDTO {
  /**
   * Constructs a new <code>AlbumDTO</code>.
   * @alias module:model/AlbumDTO
   * @class
   * @extends module:model/ArticleDTO
   * @param genre {} 
   * @param artists {} 
   * @param title {} 
   * @param releaseDate {} 
   * @param musicbrainzId {} 
   * @param label {} 
   * @param id {} 
   * @param type {} 
   * @param mediums {} 
   * @param songs {} 
   */
  constructor(genre, artists, title, releaseDate, musicbrainzId, label, id, type, mediums, songs) {
    super(genre, artists, title, releaseDate, musicbrainzId, label, id, type);
    this.mediums = mediums;
    this.songs = songs;
  }

  /**
   * Constructs a <code>AlbumDTO</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/AlbumDTO} obj Optional instance to populate.
   * @return {module:model/AlbumDTO} The populated <code>AlbumDTO</code> instance.
   */
  static constructFromObject(data, obj) {
    if (data) {
      obj = obj || new AlbumDTO();
      ArticleDTO.constructFromObject(data, obj);
      if (data.hasOwnProperty('mediums'))
        obj.mediums = ApiClient.convertToType(data['mediums'], [MediumDTO]);
      if (data.hasOwnProperty('songs'))
        obj.songs = ApiClient.convertToType(data['songs'], [SongDTO]);
    }
    return obj;
  }
}

/**
 * @member {Array.<module:model/MediumDTO>} mediums
 */
AlbumDTO.prototype.mediums = undefined;

/**
 * @member {Array.<module:model/SongDTO>} songs
 */
AlbumDTO.prototype.songs = undefined;

