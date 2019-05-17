import { Moment } from 'moment';
import { IMediaPaasho } from 'app/shared/model/media-paasho.model';
import { ICategoryPaasho } from 'app/shared/model/category-paasho.model';
import { IRatingPaasho } from 'app/shared/model/rating-paasho.model';
import { IFactorPaasho } from 'app/shared/model/factor-paasho.model';
import { IUser } from 'app/shared/model/user.model';

export const enum PriceType {
  FREE = 'FREE',
  DUTCH = 'DUTCH',
  MONETARY = 'MONETARY'
}

export const enum EventStatus {
  OPEN = 'OPEN',
  CLOSE = 'CLOSE'
}

export interface IEventPaasho {
  id?: number;
  eventTime?: Moment;
  description?: string;
  code?: string;
  title?: string;
  maxAge?: number;
  minAge?: number;
  priceType?: PriceType;
  status?: EventStatus;
  address?: string;
  visitCount?: number;
  latitude?: number;
  longitude?: number;
  likes?: number;
  filesContentType?: string;
  files?: any;
  tel?: string;
  instagram?: string;
  telegram?: string;
  capacity?: number;
  customTitle?: string;
  dateString?: string;
  timeString?: string;
  medias?: IMediaPaasho[];
  categories?: ICategoryPaasho[];
  rates?: IRatingPaasho[];
  factors?: IFactorPaasho[];
  titlesId?: number;
  participants?: IUser[];
  creatorLogin?: string;
  creatorId?: number;
}

export const defaultValue: Readonly<IEventPaasho> = {};
