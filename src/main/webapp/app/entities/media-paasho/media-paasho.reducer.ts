import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMediaPaasho, defaultValue } from 'app/shared/model/media-paasho.model';

export const ACTION_TYPES = {
  SEARCH_MEDIA: 'media/SEARCH_MEDIA',
  FETCH_MEDIA_LIST: 'media/FETCH_MEDIA_LIST',
  FETCH_MEDIA: 'media/FETCH_MEDIA',
  CREATE_MEDIA: 'media/CREATE_MEDIA',
  UPDATE_MEDIA: 'media/UPDATE_MEDIA',
  DELETE_MEDIA: 'media/DELETE_MEDIA',
  RESET: 'media/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMediaPaasho>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MediaPaashoState = Readonly<typeof initialState>;

// Reducer

export default (state: MediaPaashoState = initialState, action): MediaPaashoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MEDIA):
    case REQUEST(ACTION_TYPES.FETCH_MEDIA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEDIA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEDIA):
    case REQUEST(ACTION_TYPES.UPDATE_MEDIA):
    case REQUEST(ACTION_TYPES.DELETE_MEDIA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MEDIA):
    case FAILURE(ACTION_TYPES.FETCH_MEDIA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEDIA):
    case FAILURE(ACTION_TYPES.CREATE_MEDIA):
    case FAILURE(ACTION_TYPES.UPDATE_MEDIA):
    case FAILURE(ACTION_TYPES.DELETE_MEDIA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MEDIA):
    case SUCCESS(ACTION_TYPES.FETCH_MEDIA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDIA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEDIA):
    case SUCCESS(ACTION_TYPES.UPDATE_MEDIA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEDIA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/media';
const apiSearchUrl = 'api/_search/media';

// Actions

export const getSearchEntities: ICrudSearchAction<IMediaPaasho> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MEDIA,
  payload: axios.get<IMediaPaasho>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IMediaPaasho> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEDIA_LIST,
  payload: axios.get<IMediaPaasho>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMediaPaasho> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEDIA,
    payload: axios.get<IMediaPaasho>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMediaPaasho> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEDIA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMediaPaasho> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEDIA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMediaPaasho> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEDIA,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
