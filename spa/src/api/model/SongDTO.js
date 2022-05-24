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
import {AlbumDTO} from './AlbumDTO';
import {ArticleDTO} from './ArticleDTO';
import {ArtistDTO} from './ArtistDTO';
import {SongDTOLength} from './SongDTOLength';

/**
 * The SongDTO model module.
 * @module model/SongDTO
 * @version 3.0.1
 */
export class SongDTO extends ArticleDTO {
  /**
   * Constructs a new <code>SongDTO</code>.
   * @alias module:model/SongDTO
   * @class
   * @extends module:model/ArticleDTO
   * @param id {} 
   * @param label {} 
   * @param title {} 
   * @param releaseDate {} 
   * @param genre {} 
   * @param musicbrainzId {} 
   * @param artists {} 
   * @param type {} 
   * @param length {} 
   * @param albums {} 
   */
  constructor(id, label, title, releaseDate, genre, musicbrainzId, artists, type, length, albums) {
    super(id, label, title, releaseDate, genre, musicbrainzId, artists, type);
    this.length = length;
    this.albums = albums;
  }

  /**
   * Constructs a <code>SongDTO</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/SongDTO} obj Optional instance to populate.
   * @return {module:model/SongDTO} The populated <code>SongDTO</code> instance.
   */
  static constructFromObject(data, obj) {
    if (data) {
      obj = obj || new SongDTO();
      ArticleDTO.constructFromObject(data, obj);
      if (data.hasOwnProperty('length'))
        obj.length = SongDTOLength.constructFromObject(data['length']);
      if (data.hasOwnProperty('albums'))
        obj.albums = ApiClient.convertToType(data['albums'], [AlbumDTO]);
    }
    return obj;
  }
}

/**
 * @member {module:model/SongDTOLength} length
 */
SongDTO.prototype.length = undefined;

/**
 * @member {Array.<module:model/AlbumDTO>} albums
 */
SongDTO.prototype.albums = undefined;

